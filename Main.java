

import Src.auth.AuthService;

public class Main {
    public static void main(String[] args) {
       System.out.println(AuthService.hasher("admin"));
       System.out.println(AuthService.hasher("1234"));
             
    }

}