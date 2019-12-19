export class Constants {
  
  private static APIHOST = 'localhost';
  private static APIPORT = '8080';
  private static APIVERSION = 'v1';
  public static APIBASEURL = `http://${Constants.APIHOST}:${Constants.APIPORT}`;
  public static APIURL = `${Constants.APIBASEURL}/api/${Constants.APIVERSION}`

  private static APPHOST = 'localhost';
  private static APPPORT = '8081';
  public static CALLBACKURL = 'authorize';
  public static APPBASEURL = `http://${Constants.APPHOST}:${Constants.APPPORT}`;

  public static FACEBOOKURL = `${Constants.APIBASEURL}/oauth2/authorize/facebook?redirect_uri=${Constants.APPBASEURL}/${Constants.CALLBACKURL}`;
  public static GOOGLEURL = `${Constants.APIBASEURL}/oauth2/authorize/google?redirect_uri=${Constants.APPBASEURL}/${Constants.CALLBACKURL}`;
  public static GITHUBURL = `${Constants.APIBASEURL}/oauth2/authorize/github?redirect_uri=${Constants.APPBASEURL}/${Constants.CALLBACKURL}`;

  public static TOKENEXPIRATION = 3600000;
  public static REMEMBERMEEXPIRATION = 9999999;
}
