import java.util.*;

public class Main {
    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);
        boolean flag = true;

        while(flag)
        {
            System.out.println("\n- Select an option -");
            System.out.println("    Trainer | 1");
            System.out.println("     Player | 2");
            System.out.println("       Exit | 0");
            System.out.print("your option : ");
            switch(sc.nextInt())
            {
                case 0:
                    flag = false;
                    break;
                case 1:
                    Admin admin = new Admin();
                    admin.adminFunction();
                    break;
                case 2:
                    User user = new User();
                    user.userFunction();
                    break;
                default:
                    System.out.println("....!");
            }

        }

    }
}
