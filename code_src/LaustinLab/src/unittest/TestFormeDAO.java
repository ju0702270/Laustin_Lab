package unittest;
/**
 * Author : Rochez Justin, Sapin Laurent
 *     Creation : 21/02/2021
 *     Modification : 22/02/2021
 *     Revision : 1.0
 *     Description : Class de Test unitaire pour FormeDAO
 */
import DAO.DAOFactory;
import DAO.FormeDAO;
import exceptions.ConstraintException;
import exceptions.DBException;
import model.Etat;
import model.Forme;
import model.MedicamentConcentration;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class TestFormeDAO {

    private static FormeDAO fDAO;
    private ArrayList<Forme> lstForme;
    private Forme forme;
    /**
     * methode executée une fois avant toutes les autres méthodes.
     * Cette méthode va contruire le DAO.
     */
    @BeforeClass
    public static void initialiser(){
        fDAO = DAOFactory.getFormeDAO();
    }
    /**
     * Methode loadData, executée avant chaque méthode
     * Cette méthode va mettre à jour l'objet afin que cette objet soit celui avec l'id le plus grand.
     * Cette méthode permettra donc d'avoir un objet de test toujours à jour
     * @throws DBException
     */
    @Before
    public void loadData() throws DBException {
        lstForme= fDAO.findAll();
        forme = lstForme.get(lstForme.size()-1);
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
        Forme f = new Forme(-1,"testforme", Etat.LIQUIDE.getMinuscule());
        assertTrue(fDAO.create(f));
        loadData();
        assertEquals("testforme",forme.getLibelle());
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
        assertEquals(Etat.LIQUIDE.getMinuscule(),forme.getEtat());
        assertEquals("liquide", forme.getEtat()); // petit test de Etat
        forme.setEtat(Etat.SOUPLE.getMinuscule());
        assertTrue(fDAO.update(forme));
        loadData();
        assertEquals(Etat.SOUPLE.getMinuscule(), forme.getEtat());
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
        //creation de contrainte d'intégrité
        MedicamentConcentration mc = DAOFactory.getMedicamentConcentrationDAO().find(33);
        mc.setForme(forme);
        assertTrue(DAOFactory.getMedicamentConcentrationDAO().update(mc));
        assertFalse(fDAO.delete(forme));
    }
    /**
     * Methode de test de suppression de la base de donnée
     * @throws DBException
     * @throws ConstraintException
     */
    @Test
    public void ddelete() throws DBException, ConstraintException {
        System.out.println("true delete");
        //retrait de la contrainte d'intégrité
        MedicamentConcentration mc = DAOFactory.getMedicamentConcentrationDAO().find(33);
        mc.setForme(fDAO.find(1));
        assertTrue(DAOFactory.getMedicamentConcentrationDAO().update(mc));
        assertTrue(fDAO.delete(forme));
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("unittest.TestFormeDAO");
    }
}
