public enum enumRequest {
    pagesPages("/magnoliaPublic/.rest/pages"),
    pagesHeaderSettings("/magnoliaPublic/.rest/pages/settings/header_settings"),
    pagesFooterSettings("/magnoliaPublic/.rest/pages/settings/footer_settings"),
    pagesPLPSettings("/magnoliaPublic/.rest/pages/settings/plp_settings"),
    pagesPDPSettings("/magnoliaPublic/.rest/pages/pdp_settings"),
    pagesSRPSettings("/magnoliaPublic/.rest/pages/settings/srp_settings"),
    pagesMainWeb("/magnoliaPublic/.rest/pages/settings/main_web"),
    pagesMainMWeb("/magnoliaPublic/.rest/pages/settings/main_mweb"),
    pagesMainApp("/magnoliaPublic/.rest/pages/settings/main_app"),
    pagesCartSettings("/magnoliaPublic/.rest/pages/settings/cart_settings"),
    pagesComparisonSettings("/magnoliaPublic/.rest/pages/settings/comparison_settings"),
    pagesFavoritesSettings("/magnoliaPublic/.rest/pages/settings/favorites_settings"),
    pagesPromo2Settings("/magnoliaPublic/.rest/pages/promo_2_0_settings"),
    pagesServicesSettings("/magnoliaPublic/.rest/pages/services_settings"),
    pagesSelfServiceWebSettings("/magnoliaPublic/.rest/pages/self_service_web_settings"),
    pagesSelfServiceAppSettingsPage("/magnoliaPublic/.rest/pages/self_service_app_settings_page"),
    pagesPromoPages("/magnoliaPublic/.rest/pages/promo-pages"),
    pagesPDPPromoBlocks("/magnoliaPublic/.rest/pages/pdp-promo-blocks"),
    pagesPersonalOffers("/magnoliaPublic/.rest/pages/personal-offers"),
    pagesServicesDescriptions("/magnoliaPublic/.rest/pages/services_descriptions"),
    pagesBanner("/magnoliaPublic/.rest/delivery/banner"),
    productsSetsProductSet("/magnoliaPublic/.rest/delivery/product-sets"),
    productsSetsDailyProductSets("/magnoliaPublic/.rest/delivery/daily-product-sets"),
    productsSetsPromoPageSets("/magnoliaPublic/.rest/delivery/promo-page-sets"),
    productsSetsPopularCategory("/magnoliaPublic/.rest/delivery/popularCategory"),
    navigationNavigationLinkSite("/magnoliaPublic/.rest/delivery/navigationLinkSite"),
    navigationNavigationLinkMobile("/magnoliaPublic/.rest/delivery/navigationLinkMobile"),
    navigationNavigationPromoActions("/magnoliaPublic/.rest/delivery/navigationPromoActions"),
    navigationTopPromoLinks("/magnoliaPublic/.rest/delivery/topPromoLinks"),
    navigationBottomPromoLinks("/magnoliaPublic/.rest/delivery/bottomPromoLinks"),
    seoRedirects("/magnoliaPublic/.rest/delivery/redirects"),
    seoRobots("/magnoliaPublic/.rest/delivery/robots"),
    pdpFirstToKnow("/magnoliaPublic/.rest/delivery/first-to-know"),
    pdpProductNotification("/magnoliaPublic/.rest/delivery/product-notification"),
    pdpBlockPriority("/magnoliaPublic/.rest/delivery/block-priority"),
    colorsApp("/magnoliaPublic/.rest/color-mappings/v1/map"),
    brandApp("/magnoliaPublic/.rest/delivery/brands"),
    appInStore("/magnoliaPublic/.rest/delivery/store-departments"),
    plpListings("/magnoliaPublic/.rest/delivery/plp-listings"),
    servicesAppServiceGroups("/magnoliaPublic/.rest/delivery/service-groups");

    private String title;

    enumRequest(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return title;
    }

    }
