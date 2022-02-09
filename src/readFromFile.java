import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class readFromFile {

    private static postgreSQLConnection connection = new postgreSQLConnection();
    private static int counter = 0;
    private static String sql = "";
    private static Date start = new Date();
    private static Date end = new Date(0);

    public static void main(String[] args) throws Exception
    {
        List<String> files = fileList("/home/wolf/IdeaProjects/logParser/logs");

        for (String s: files) {
            readFile(s);
        }

//        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
//        start = format.parse("2021-11-30 15:25:09");
//        end = format.parse("2021-12-01 09:18:26");
//        cleanDB(end, start);
        addReport(start, end);
        cleanDB(start, end);
        getReport(start, end);

//        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
//        Date sdate = format.parse("2022-feb-04 00:00:00");
//        Date edate = format.parse("2022-feb-05 00:00:00");
//        getReport(sdate, edate);
    }

    public static List<String> fileList(String dir) throws IOException {
        List<String> result = new ArrayList<>();

        Path path = Path.of(dir);

        try (DirectoryStream<Path> files = Files.newDirectoryStream(path)) {
            for (Path pathFile : files)
                result.add(pathFile.toString());
        }
        return result;
    }

    public static void readFile(String pathFile) throws ParseException {
        String line = "";
        char[] chars = new char[10000];
        int i;
        boolean f = false;

        try(FileReader reader = new FileReader(pathFile))
        {
            int c;
            i = 0;
            while((c=reader.read())!=-1){

                chars[i] = (char) c;
                if (c == 10)
                {
                    line = String.valueOf(chars, 0, i - 1);
                    if (!f)
                        f = true;
                    else
                        putInDBEvent(parseLine(line));
                    i = -1;
                    chars = new char[10000];
                }
                ++i;
            }
            putInDBEvent(null);
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }

    public static logEvent parseLine(String line) throws ParseException {
        DateFormat format = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss", Locale.ENGLISH);
        Date date = format.parse(line.substring(line.indexOf("[") + 1, line.indexOf(" +")));

        if (date.before(start))
            start = date;
        if (date.after(end))
            end = date;

        line = line.substring(line.indexOf("]") + 2);
        String r = line.substring(line.indexOf("magnoliaPublic/") + 14, !line.contains("?") ? line.indexOf("HTTP") - 1 : line.indexOf("?"));
        if (r.indexOf("/dam/jcr") > -1)
            r = "/dam/jcr";
        else if (r.indexOf("/.rest/pages/promo_pages") > -1)
            r = "/.rest/pages/promo_pages";
        else if (r.indexOf("/.rest/delivery/promo-page-sets") > -1)
            r = "/.rest/delivery/promo-page-sets";
        else if (r.indexOf("/.rest/pages/personal_offers") > -1)
            r = "/.rest/pages/personal_offers";
        else if (r.indexOf("/.healthcheck") > -1)
            r = "/.healthcheck";
//        if (r.equals(""))
//            System.out.println("line = " + line);
        if (r.length() > 50){
            System.out.println("r = " + line);
            r = r.substring(0, 49);
        }


        line = line.substring(line.indexOf("HTTP") + 10);
        int s = Integer.parseInt(line.substring(0, line.indexOf(" ")));
        int dur = Integer.parseInt(line.substring(line.lastIndexOf(" ") + 1, line.lastIndexOf("m")));

        return new logEvent(date, r, s, dur);
    }

    public static void putInDBEvent(logEvent event)
    {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        if (event == null)
        {
            connection.executeInsert(sql);
            counter = 0;
            sql = "";
            return;
        }
        else if (counter == 1000)
        {
            connection.executeInsert(sql);
//            System.out.println(format.format(event.getTime()));
            counter = 0;
            sql = "";
        }

        // intoDB
        Formatter formatter = new Formatter();
        formatter.format("INSERT INTO stat (date, url, responsetime)" +
                " VALUES ('%s', '%s', %d); ", format.format(event.getTime()), event.getRequest(), event.getDuration());
        sql += formatter;
        ++counter;
    }

    public static void cleanDB(Date start, Date end)
    {
        Formatter formatter = new Formatter();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        formatter.format("DELETE FROM stat WHERE date >= '%s' AND date <= '%s'; ",
                format.format(start), format.format(end));
        sql += formatter;
        connection.executeInsert(sql);
    }

    public static void addReport(Date start, Date end){
        Formatter formatter = new Formatter();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        formatter.format("select url, MIN(t.rps) AS minRps, MAX(t.rps) AS maxRps, ROUND(AVG(t.rps)) AS avgRps, " +
                        "MAX(t.responsetime) AS maxRT, ROUND(PERCENTILE_DISC(0.95) WITHIN GROUP " +
                        "(ORDER BY t.responsetime)) AS p95, ROUND(PERCENTILE_DISC(0.99) WITHIN GROUP " +
                        "(ORDER BY t.responsetime)) AS p99 FROM (select a.url AS url, a.date AS date, a.responsetime " +
                        "AS responsetime, b.rps AS rps from (select url, date, responsetime from stat where date>='%s' " +
                        "AND date <= '%s') a left join (select url, date, COUNT(responsetime) AS rps from stat " +
                        "where date>='%s' AND date <= '%s' group by url, date) b " +
                        "on a.url = b.url and a.date = b.date) t GROUP BY url;",
                format.format(start), format.format(end), format.format(start), format.format(end));
        String sql = "" + formatter;
        ResultSet resultSet = connection.executeSelect(sql);

        formatter = new Formatter();
        sql = "";
        try {
            while (resultSet.next()) {
                formatter.format("INSERT INTO reports (start_time, end_time, url, minRPS, maxRPS, avgRPS, maxRT, p95, " +
                                "p99) VALUES (to_timestamp('%s', 'yyyy-mm-dd hh24:mi:ss'), " +
                                "to_timestamp('%s', 'yyyy-mm-dd hh24:mi:ss'), '%s', %d, %d, %d, %d, %d, %d); ",
                        format.format(start), format.format(end), resultSet.getString("url").trim(),
                        resultSet.getInt("minRps"), resultSet.getInt("maxRps"), resultSet.getInt("avgRps"),
                        resultSet.getInt("maxRT"), resultSet.getInt("p95"), resultSet.getInt("p99"));
            }
            sql += formatter;

            connection.resultSet.close();
            connection.statement.close();
            connection.executeInsert(sql);
            connection.conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void getReport(Date start, Date end){
        String url;
        String start_time;
        String end_time;
        int minRps;
        int maxRps;
        int avgRps;
        int maxRT;
        int p95;
        int p99;

        Formatter formatter = new Formatter();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        formatter.format("select start_time, end_time, url, minRPS, maxRPS, avgRPS, maxRT, p95, p99 from " +
                        "reports where start_time >= '%s' AND end_time <= '%s' ORDER BY url, start_time, end_time; ",
                        format.format(start), format.format(end));
        String sql = "" + formatter;
        ResultSet resultSet = connection.executeSelect(sql);

        String fileName = "/home/wolf/IdeaProjects/logParser/reports/report_" + format.format(start) + "_" + format.format(end) + ".csv";
        try(FileWriter writer = new FileWriter(fileName, false))
        {
            writer.write("start_time,end_time,url,minRps,maxRps,avgRps,maxRT,p95,p99\n");
            try {
                while (resultSet.next()) {
                    start_time = format.format(resultSet.getTimestamp("start_time"));
                    end_time = format.format(resultSet.getTimestamp("end_time"));
                    url = resultSet.getString("url").trim();
                    minRps = resultSet.getInt("minRps");
                    maxRps = resultSet.getInt("maxRps");
                    avgRps = resultSet.getInt("avgRps");
                    maxRT = resultSet.getInt("maxRT");
                    p95 = resultSet.getInt("p95");
                    p99 = resultSet.getInt("p99");

                    writer.write(start_time + "," + end_time + "," + url + "," + minRps + "," + maxRps + ","
                            + avgRps + "," + maxRT + "," + p95 + "," + p99 + "\n");
                }

                writer.flush();
                connection.resultSet.close();
                connection.statement.close();
                connection.conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
