package src;

import src.client.Client;
import src.client.view.MainFrame;


public class Main {
    public static void main(String[] args) {

        try {
            MainFrame mainFrame = new MainFrame(new Client("Alex"));
        } catch (Exception e) {
            e.printStackTrace();
        }
//        try {
//            MainFrame mainFrame = new MainFrame(new Client("Sveta"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
