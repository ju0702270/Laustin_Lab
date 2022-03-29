package unittest;

import DAO.ConcentrationDAO;
import DAO.DAOFactory;
import exceptions.ConstraintException;
import exceptions.DBException;
import model.Concentration;
import model.MedicamentConcentration;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Author : Rochez Justin, Sapin Laurent
 *     Creation : 21/02/2021
 *     Modification : 22/02/2021
 *     Revision : 1.0
 *     Description : Class de Test unitaire pour ConcentrationDAO
 */
public class TestConcentrationDAO {

    private static ConcentrationDAO cDAO;
    private ArrayList<Concentration> lstConc;
    private Concentration concentration;

    /**
     * methode executée une fois avant toutes les autres méthodes.
     * Cette méthode va contruire le DAO.
     */
    @BeforeClass
    public static void initialiser(){
        cDAO = DAOFactory.getConcentrationDAO();

    }
    /**
     * Methode loadData, executée avant chaque méthode
     * Cette méthode va mettre à jour l'objet afin que cette objet soit celui avec l'id le plus grand.
     * Cette méthode permettra donc d'avoir un objet de test toujours à jour
     * @throws DBException
     */
    @Before
    public void loadData() throws DBException {
        lstConc= cDAO.findAll();
        concentration = lstConc.get(lstConc.size()-1);
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
        Concentration c = new Concentration(-1,"tst",1.1);
        assertTrue(cDAO.create(c));
        loadData();
        assertEquals("tst",concentration.getSymbole());
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
        assertEquals("tst",concentration.getSymbole());
        concentration.setSymbole("symb");
        assertTrue(cDAO.update(concentration));
        loadData();
        assertEquals("symb",concentration.getSymbole());
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
        MedicamentConcentration mc = DAOFactory.getMedicamentConcentrationDAO().find(33);
        mc.setConcentration(concentration);
        // création d'une contrainte d'intégrité pour concentration
        assertTrue(DAOFactory.getMedicamentConcentrationDAO().update(mc));
        // preuve qu'il est impossible de supprimer la concentration
        assertFalse( cDAO.delete(concentration) );
    }
    /**
     * Methode de test de suppression de la base de donnée
     * @throws DBException
     * @throws ConstraintException
     */
    @Test
    public void ddelete() throws DBException, ConstraintException {
        System.out.println("true delete");
        MedicamentConcentration mc = DAOFactory.getMedicamentConcentrationDAO().find(33);
        mc.setConcentration(cDAO.find(6));
        // retrait de la contrainte d'intégrité
        assertTrue(DAOFactory.getMedicamentConcentrationDAO().update(mc));
        assertTrue(cDAO.delete(concentration));
        loadData();
        assertNotEquals("symb",concentration.getSymbole());
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("unittest.TestConcentrationDAO");
    }

}
