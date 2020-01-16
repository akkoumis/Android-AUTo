package cy.com.talaiporoi.authauto;

public class Api {

    private static final String ROOT_URL = "http://10.0.2.2/authauto/v1/Api.php?apicall=";

    public static final String URL_CREATE_CUSTOMER = ROOT_URL + "createcustomer";
    public static final String URL_READ_CUSTOMERS = ROOT_URL + "getcustomers";
    public static final String URL_READ_LOGIN = ROOT_URL + "getlogin";


    public static final String URL_UPDATE_HERO = ROOT_URL + "updatehero";
    public static final String URL_DELETE_HERO = ROOT_URL + "deletehero&id=";

}