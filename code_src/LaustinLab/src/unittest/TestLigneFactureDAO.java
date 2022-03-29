package unittest;
/**
 * Author : Rochez Justin, Sapin Laurent
 *     Creation : 21/02/2021
 *     Modification : 22/02/2021
 *     Revision : 1.0
 *     Description : Class de Test unitaire pour LigneFactureDAO
 */

import DAO.DAOFactory;
import DAO.LigneFactureDAO;
import exceptions.ConstraintException;
import exceptions.DBException;
import model.Facture;
import model.LigneFacture;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import utility.Utility;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class TestLigneFactureDAO {

    private static LigneFactureDAO lfDAO;
    private ArrayList<LigneFacture> lstLiFac;
    private LigneFacture liFacture;
    /**
     * methode executée une fois avant toutes les autres méthodes.
     * Cette méthode va contruire le DAO.
     */
    @BeforeClass
    public static void initialiser(){
        lfDAO = DAOFactory.getLigneFactureDAO();
    }
    /**
     * Methode loadData, executée avant chaque méthode
     * Cette méthode va mettre à jour l'objet afin que cette objet soit celui avec l'id le plus grand.
     * Cette méthode permettra donc d'avoir un objet de test toujours à jour
     * @throws DBException
     */
    @Before
    public void loadData() throws DBException {
        lstLiFac = lfDAO.findAll();
        ArrayList<Facture> lstFact = DAOFactory.getFactureDAO().findAll();
        Facture fact = lstFact.get(lstFact.size()-1);
        liFacture = lfDAO.find(fact,DAOFactory.getMedicamentConcentrationDAO().find(44));
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
        ArrayList<Facture> lstFact = DAOFactory.getFactureDAO().findAll();
        Facture fact = lstFact.get(lstFact.size()-1);
        LigneFacture li = new LigneFacture(fact,DAOFactory.getMedicamentConcentrationDAO().find(44),10,
                Utility.getTotalLiFact(DAOFactory.getMedicamentConcentrationDAO().find(44),10));
        assertTrue(lfDAO.create(li));
        loadData();
        assertEquals(liFacture.getQuantite(),10);

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
        ArrayList<Facture> lstFact = DAOFactory.getFactureDAO().findAll();
        Facture fact = lstFact.get(lstFact.size()-1);
        //on verifie que l'id est identique
        assertEquals(liFacture.getFacture().getId(),
                lfDAO.find(fact,DAOFactory.getMedicamentConcentrationDAO().find(44)).getFacture().getId());
        assertEquals(liFacture.getQuantite(), 10);
        liFacture.setQuantite(24);
        assertTrue(lfDAO.update(liFacture));
        loadData();
        assertEquals(liFacture.getQuantite(), 24);
    }
    /**
     * Methode de test de suppression de la base de donnée
     * @throws DBException
     * @throws ConstraintException
     */
    @Test
    public void cdelete() throws DBException, ConstraintException {
        System.out.println("delete");
        assertTrue(lfDAO.delete(liFacture));
        loadData();
        assertNull(liFacture);
    }
    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("unittest.TestLigneFactureDAO");
    }
}
