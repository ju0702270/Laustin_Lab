package unittest;
/**
 * Author : Rochez Justin, Sapin Laurent
 *     Creation : 21/02/2021
 *     Modification : 22/02/2021
 *     Revision : 1.0
 *     Description : Class de Test unitaire pour ConditionnementDAO
 */
import DAO.ConditionnementDAO;
import DAO.DAOFactory;
import exceptions.ConstraintException;
import exceptions.DBException;
import model.Conditionnement;
import model.MedicamentConcentration;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class TestConditionnementDAO {

    private static ConditionnementDAO cDAO;
    private ArrayList<Conditionnement> lstCond;
    private Conditionnement conditionnement;
    /**
     * methode executée une fois avant toutes les autres méthodes.
     * Cette méthode va contruire le DAO.
     */
    @BeforeClass
    public static void initialiser(){
        cDAO = DAOFactory.getConditionnementDAO();
    }
    /**
     * Methode loadData, executée avant chaque méthode
     * Cette méthode va mettre à jour l'objet afin que cette objet soit celui avec l'id le plus grand.
     * Cette méthode permettra donc d'avoir un objet de test toujours à jour
     * @throws DBException
     */
    @Before
    public void loadData() throws DBException {
        lstCond= cDAO.findAll();
        conditionnement = lstCond.get(lstCond.size()-1);
    }
    /**
     * Methode de test pour la création de l'objet dans la base de donnée.
     * cette méthode vérifie si le create retourne bien true à la création et ensuite verifira
     * la correspondance d'un attribut de l'objet (mis à jour)
     * @throws DBException
     */
    @Test
    public void acreate() throws DBException, ConstraintException {
        System.out.println("create");
        Conditionnement c = new Conditionnement(-1,666);
        assertTrue(cDAO.create(c));
        loadData();
        assertEquals(666,conditionnement.getUnite());
    }
    /**
     * Méthode de test pour l'update de l'objet dans la base de donnée.
     * cette méthode verifie si l'objet correspond à ce que l'on attend.
     * un attribut de l'objet est mis à jour et updaté dans la base de donnée
     * la méthode termine avec une verification de la modification dans la base de donnée
     * @throws DBException
     */
    @Test
    public void bupdate() throws DBException, ConstraintException {
        System.out.println("update");
        assertEquals(666,conditionnement.getUnite());
        conditionnement.setUnite(6969);
        assertTrue(cDAO.update(conditionnement));
        loadData();
        assertEquals(6969, conditionnement.getUnite());
    }
    /**
     * Methode de test de non suppression de la base de donnée
     * une contrainte d'intégrité est mise en place pour empecher la suppression
     * @throws DBException
     * @throws ConstraintException
     */
    @Test(expected = ConstraintException.class)
    public void cdelete() throws DBException, ConstraintException {
        System.out.println("delete");
        //creation d'un contrainte d'intégrité
        MedicamentConcentration mc = DAOFactory.getMedicamentConcentrationDAO().find(33);
        mc.setConditionnement(conditionnement);
        assertTrue(DAOFactory.getMedicamentConcentrationDAO().update(mc));
        assertFalse(cDAO.delete(conditionnement));
    }
    /**
     * Methode de test de suppression de la base de donnée
     * @throws DBException
     * @throws ConstraintException
     */
    @Test
    public void ddelete() throws DBException, ConstraintException {
        System.out.println("true delete");
        //creation d'un contrainte d'intégrité
        MedicamentConcentration mc = DAOFactory.getMedicamentConcentrationDAO().find(33);
        mc.setConditionnement(cDAO.find(1));
        assertTrue(DAOFactory.getMedicamentConcentrationDAO().update(mc));
        assertTrue(cDAO.delete(conditionnement));
    }


    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("unittest.TestConditionnementDAO");
    }
}
