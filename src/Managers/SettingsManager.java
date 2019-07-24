package Managers;

public class SettingsManager 
{
    private static String DBConnString;
    
    public static void setDBConnString(String conn) { DBConnString = conn; }
    public static String getDBConnString() { return DBConnString; }
}
