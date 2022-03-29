package unittest;


import DAO.DAOFactory;
import DAO.LotDAO;
import DAO.MedicamentConcentrationDAO;
import exceptions.ConstraintException;
import exceptions.DBException;
import model.LigneFacture;
import model.Lot;
import model.MedicamentConcentration;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import utility.Utility;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static java.time.LocalTime.now;
import static org.junit.Assert.*;
import static utility.Utility.addDay;
import static utility.Utility.today;
/**
 * Author : Rochez Justin, Sapin Laurent
 *     Creation : 21/02/2021
 *     Modification : 22/02/2021
 *     Revision : 1.0
 *     Description : Class de Test unitaire pour MedicamentCOncentrationDAO
 */
public class TestMedicamentConcentrationDAO {

    private static MedicamentConcentrationDAO mcDAO;
    private ArrayList<MedicamentConcentration> lstMedConc;
    private MedicamentConcentration medConc;
    /**
     * methode executée une fois avant toutes les autres méthodes.
     * Cette méthode va contruire le DAO.
     */
    @BeforeClass
    public static void initialiser(){
        mcDAO = DAOFactory.getMedicamentConcentrationDAO();
    }
    /**
     * Methode loadData, executée avant chaque méthode
     * Cette méthode va mettre à jour l'objet afin que cette objet soit celui avec l'id le plus grand.
     * Cette méthode permettra donc d'avoir un objet de test toujours à jour
     * @throws DBException
     */
    @Before
    public void loadData() throws DBException {
        lstMedConc = mcDAO.findAllWithoutLot();
        medConc = lstMedConc.get(lstMedConc.size()-1);
    }
    /**
     * Methode de test pour la création de l'objet dans la base de donnée.
     * cette méthode vérifie si le create retourne bien true à la création et ensuite verifira
     * la correspondance d'un attribut de l'objet (mis à jour)
     * @throws DBException
     */
    @Test
    public void acreate() throws DBException, ConstraintException {
        MedicamentConcentration medic = new MedicamentConcentration(-1,DAOFactory.getMedicamentDAO().find(2),DAOFactory.getConcentrationDAO().find(6),
                DAOFactory.getConditionnementDAO().find(1), DAOFactory.getFormeDAO().find(31), 20.0,new ArrayList<Lot>(),21.0);

        System.out.println("create");
        assertTrue( mcDAO.create(medic) );//creation du MedicamentConcentration dans la base de donnée
        loadData();

        assertEquals(20.0, medConc.getPrix(), 0.0);
    }

    @Test
    public void acreateWithLots() throws DBException, ConstraintException {
        ArrayList<Lot> lots;
        LotDAO lotDAO = DAOFactory.getLotDAO();
        Lot lot1= new Lot(Integer.parseInt( now().toString().substring(10)+1),today(),addDay(today(),365),medConc,100) ;
        Lot lot2= new Lot(Integer.parseInt(now().toString().substring(10)+2),today(),addDay(today(),465),medConc,500) ;
        assertTrue(lotDAO.create(lot1));
        assertTrue(lotDAO.create(lot2));
        loadData();
        assertEquals(0,medConc.getStock());//vérification du stock à zero
        for ( Lot lot: medConc.getLots() ) {// vérification que chaque lot est bien celui de medConc
            assertEquals(lot.getMedicamentConcentration(), medConc );
        }
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
        assertEquals(20.0,medConc.getPrix(),0.0);
        medConc.setPrix(33.0);
        assertTrue(mcDAO.update(medConc));
        loadData();
        assertEquals(33.0, medConc.getPrix(),0.0);
    }
    /**
     * Methode de test de suppression de la base de donnée
     * avec injection d'une contrainte d'intégrité pour verifier que celle ci est gérée
     * @throws DBException
     * @throws ConstraintException
     */
    @Test
    public void cdelete() throws DBException, ConstraintException {
        System.out.println("delete");
        lstMedConc= mcDAO.findAll();
        medConc= lstMedConc.get(lstMedConc.size()-1);
        int idMedConc = medConc.getId();
        //creation de ligneFacture associée
        LigneFacture ligne = new LigneFacture(DAOFactory.getFactureDAO().find(2),medConc,3, Utility.getTotalLiFact(medConc,3));
        assertTrue(DAOFactory.getLigneFactureDAO().create(ligne));
        ligne = DAOFactory.getLigneFactureDAO().find(DAOFactory.getFactureDAO().find(2), medConc );
        //verification que ligne est bien associé à medConc
        assertEquals(ligne.getMedicamentConcentration(), medConc);

        //suppression de medConc
        //MedicamentConcentration delete va supprimer toute les lignes factures associée
        assertTrue(mcDAO.delete(medConc));
        ligne = DAOFactory.getLigneFactureDAO().find(DAOFactory.getFactureDAO().find(2), medConc );
        assertNull(ligne);
        ArrayList<Lot> lots= DAOFactory.getLotDAO().findAll(medConc);
        assertEquals(0, lots.size());//verification qu'il n'y a plus de lot assicié



    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("unittest.TestMedicamentConcentrationDAO");
    }
}
