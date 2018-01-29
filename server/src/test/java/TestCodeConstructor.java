import com.sergii.fgjx.sb.api.Messages;
import com.sergii.fgjx.sb.server.Session;

public class TestCodeConstructor {
    public static void souldTest() {
        System.out.println(new Session().generateCode(Messages.Weapon.BURRITO));
        System.out.println(new Session().generateCode(Messages.Weapon.TORPEDOES));
        System.out.println(new Session().generateCode(Messages.Weapon.PLAZMA_CANONS));
        System.out.println(new Session().generateCode(Messages.Weapon.HIGH_ENERGY_LAZER));
    }
    public static void main(String []args) {
        System.out.println("TEST");
        souldTest();
    }

}
