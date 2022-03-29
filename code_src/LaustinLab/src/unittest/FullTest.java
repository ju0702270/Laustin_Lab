package unittest;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;
/**
 *     Author : Rochez Justin, Sapin Laurent
 *     Creation : 10/03/2021
 *     Modification : 17/03/2021
 *     Revision : 1.0
 *     Description : Classe de test pour tout les tests
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        TestConditionnementDAO.class,
        TestFormeDAO.class,
        TestClientDAO.class,
        TestConcentrationDAO.class,
        TestFactureDAO.class,
        TestLigneFactureDAO.class,
        TestLotDAO.class,
        TestMedicamentConcentrationDAO.class,
        TestMedicamentDAO.class,
        TestUtilisateurDAO.class
        })
public class FullTest {

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("unittest.FullTest");
    }
}
