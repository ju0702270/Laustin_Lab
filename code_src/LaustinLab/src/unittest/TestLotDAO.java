package unittest;
/**
 * Author : Rochez Justin, Sapin Laurent
 *     Creation : 21/02/2021
 *     Modification : 22/02/2021
 *     Revision : 1.0
 *     Description : Class de Test unitaire pour LotDAO
 */
import DAO.DAOFactory;
import DAO.LotDAO;
import DAO.MedicamentConcentrationDAO;
import exceptions.ConstraintException;
import exceptions.DBException;
import model.Lot;
import model.MedicamentConcentration;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import utility.Utility;

import java.util.ArrayList;

import static java.time.LocalTime.now;
import static org.junit.Assert.*;
import static utility.Utility.addDay;
import static utility.Utility.today;

public class TestLotDAO {
    private static LotDAO lDAO;
    private ArrayList<Lot> lstLot;
    private Lot lot;
    private static int numLot;
    /**
     * methode executée une fois avant toutes les autres méthodes.
     * Cette méthode va contruire le DAO.
     */
    @BeforeClass
    public static void initialiser(){
        lDAO = DAOFactory.getLotDAO();
        numLot =Integer.parseInt(Utility.today().toString().replace("-","") + "97");

    }
    /**
     * Methode loadData, executée avant chaque méthode
     * Cette méthode va mettre à jour l'objet afin que cette objet soit celui avec l'id le plus grand.
     * Cette méthode permettra donc d'avoir un objet de test toujours à jour
     * @throws DBException
     */
    @Before
    public void loadData() throws DBException {
        lstLot = lDAO.findAll();
        lot= lstLot.get(lstLot.size()-1);
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
        Lot l1 = new Lot(numLot , Utility.today(),
                Utility.addDay(Utility.today(), 365),DAOFactory.getMedicamentConcentrationDAO().find(33),100 );
        assertTrue(lDAO.create(l1));
        loadData();
        assertEquals(numLot, lot.getId());
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
        assertEquals(numLot, lot.getId());
        lot.setQuantite(98);
        assertTrue(lDAO.update(lot));
        loadData();
        assertEquals(98, lot.getQuantite());
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
        MedicamentConcentrationDAO mcDAO = DAOFactory.getMedicamentConcentrationDAO();
        MedicamentConcentration mc = new MedicamentConcentration(-1,DAOFactory.getMedicamentDAO().find(1),
                DAOFactory.getConcentrationDAO().find(6),DAOFactory.getConditionnementDAO().find(3),DAOFactory.getFormeDAO().find(31),10.0,null,21.0);
        ArrayList<MedicamentConcentration> lstMedConc = mcDAO.findAll();
        mc = lstMedConc.get(lstMedConc.size()-1);
        ArrayList<Lot> lots;
        LotDAO lotDAO = DAOFactory.getLotDAO();
        Lot lot1= new Lot(Integer.parseInt( now().toString().substring(10)+1),today(),addDay(today(),365),mc,100) ;
        Lot lot2= new Lot(Integer.parseInt(now().toString().substring(10)+2),today(),addDay(today(),465),mc,500) ;
        assertTrue(lotDAO.create(lot1));
        assertTrue(lotDAO.create(lot2));
        lots = lotDAO.findAll(mc);
        mc.setLots(lots);
        //on supprime mc
        assertTrue(DAOFactory.getMedicamentConcentrationDAO().delete(mc));
        assertTrue(lDAO.delete(lot));
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("unittest.TestLotDAO");
    }
}
