package unittest;
/**
 * Author : Rochez Justin, Sapin Laurent
 *     Creation : 21/02/2021
 *     Modification : 22/02/2021
 *     Revision : 1.0
 *     Description : Class de Test unitaire pour FactureDAO
 */
import DAO.DAOFactory;
import DAO.FactureDAO;
import exceptions.ConstraintException;
import exceptions.DBException;
import model.Facture;
import model.LigneFacture;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.ArrayList;

import static org.junit.Assert.*;
import static utility.Utility.getTotalFact;
import static utility.Utility.getTotalLiFact;

public class TestFactureDAO {
    private static FactureDAO fDAO;
    private ArrayList<Facture> lstFac;
    private Facture facture;
    /**
     * methode executée une fois avant toutes les autres méthodes.
     * Cette méthode va contruire le DAO.
     */
    @BeforeClass
    public static void initialiser(){
        fDAO = DAOFactory.getFactureDAO();
    }
    /**
     * Methode loadData, executée avant chaque méthode
     * Cette méthode va mettre à jour l'objet afin que cette objet soit celui avec l'id le plus grand.
     * Cette méthode permettra donc d'avoir un objet de test toujours à jour
     * @throws DBException
     */
    @Before
    public void loadData() throws DBException {
        lstFac = fDAO.findAll();
        facture = lstFac.get(lstFac.size()-1);
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
        ArrayList<LigneFacture> lstLiFact = new ArrayList<>();

        LigneFacture liFac =new LigneFacture(fDAO.find(2)
                ,DAOFactory.getMedicamentConcentrationDAO().find(33),12,
                getTotalLiFact(DAOFactory.getMedicamentConcentrationDAO().find(33),10));
        lstLiFact.add(liFac);

        Facture fact = new Facture(-1,DAOFactory.getClientDAO().find(20),DAOFactory.getUtilisateurDAO().find(1),
                new Timestamp(System.currentTimeMillis()),lstLiFact,getTotalFact(lstLiFact)
                ,DAOFactory.getClientDAO().find(1));
        assertEquals(1,fact.getLigneFacture().size());
        assertTrue(fDAO.create(fact));
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
        assertEquals(20,facture.getClient().getId());
        facture.setClient(DAOFactory.getClientDAO().find(18));
        assertTrue(fDAO.update(facture));
        loadData();
        assertEquals(facture.getClient().getId(),18);
    }
    /**
     * Methode de test de suppression de la base de donnée
     * @throws DBException
     * @throws ConstraintException
     */
    @Test
    public void cdelete() throws DBException, ConstraintException {
        System.out.println("delete");
        //verification qu'il y a au moins une Ligne Facture associée
        assertNotEquals(DAOFactory.getLigneFactureDAO().findAll(facture).size(),0);
        //suppression de la facture
        assertTrue(fDAO.delete(facture));
        //verification qu'on a plus de Ligne facture associée
        assertEquals(DAOFactory.getLigneFactureDAO().findAll(facture).size(),0);
        //verification que facture n'est plus dans la base de donnée
        assertNull(fDAO.find(facture.getId()));

    }
    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("unittest.TestFactureDAO");
    }
}
