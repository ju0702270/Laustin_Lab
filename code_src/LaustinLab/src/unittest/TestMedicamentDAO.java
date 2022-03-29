package unittest;
/**
 * Author : Rochez Justin, Sapin Laurent
 *     Creation : 21/02/2021
 *     Modification : 22/02/2021
 *     Revision : 1.0
 *     Description : Class de Test unitaire pour MedicamentDAO
 */
import DAO.DAOFactory;
import DAO.LotDAO;
import DAO.MedicamentDAO;
import exceptions.ConstraintException;
import exceptions.DBException;
import model.Lot;
import model.Medicament;
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

public class TestMedicamentDAO {

    private static Medicament medicament;
    private static MedicamentDAO medDAO;
    private static ArrayList<Medicament> lstMedic;
    /**
     * methode executée une fois avant toutes les autres méthodes.
     * Cette méthode va contruire le DAO.
     */
    @BeforeClass
    public static void initialiser() throws DBException {

        medDAO = DAOFactory.getMedicamentDAO();
    }
    /**
     * Methode loadData, executée avant chaque méthode
     * Cette méthode va mettre à jour l'objet afin que cette objet soit celui avec l'id le plus grand.
     * Cette méthode permettra donc d'avoir un objet de test toujours à jour
     * @throws DBException
     */
    @Before
    public void loadData() throws DBException {
        lstMedic = medDAO.findAll();
        medicament = lstMedic.get(lstMedic.size()-1); // on reprend la derniere entrée de Médicament dans le DB
    }
    /**
     * Methode de test pour la création de l'objet dans la base de donnée.
     * cette méthode vérifie si le create retourne bien true à la création et ensuite verifira
     * la correspondance d'un attribut de l'objet (mis à jour)
     * @throws DBException
     */
    @Test
    public void acreate() throws DBException, ConstraintException {
        medicament = new Medicament(-1, true, Utility.today(), "Test");
        System.out.println("create");
        assertTrue(medDAO.create(medicament));
        lstMedic = medDAO.findAll();
        medicament = lstMedic.get(lstMedic.size()-1); // on reprend la derniere entrée de Médicament dans le DB
        assertEquals(medicament.getLibelle(),"Test");
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
        assertEquals(medicament.getLibelle(), "Test");
        medicament.setLibelle("MedicamentUpdate");
        assertTrue(medDAO.update(medicament));
        medicament = medDAO.find(medicament.getId()); // on reprend la derniere entrée de Médicament dans le DB
        assertEquals(medicament.getLibelle(), "MedicamentUpdate");
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
         lstMedic = medDAO.findAll();
         medicament = lstMedic.get(lstMedic.size()-1); // on reprend la derniere entrée de Médicament dans le DB

        //creation d'un MedicamentConcentration de ce Medicament
        MedicamentConcentration medConc = new MedicamentConcentration(-1,medicament,DAOFactory.getConcentrationDAO().find(6),DAOFactory.getConditionnementDAO().find(1),
                 DAOFactory.getFormeDAO().find(31), 20.0,null,21.0);
        assertTrue(DAOFactory.getMedicamentConcentrationDAO().create(medConc));
        ArrayList<MedicamentConcentration> lstMedicConc= DAOFactory.getMedicamentConcentrationDAO().findAllWithoutLot();
        medConc= lstMedicConc.get(lstMedicConc.size()-1);
        //attribution des lots
        ArrayList<Lot> lots;
        LotDAO lotDAO = DAOFactory.getLotDAO();
        Lot lot1= new Lot(Integer.parseInt( now().toString().substring(10)+1),today(),addDay(today(),365),medConc,100) ;
        Lot lot2= new Lot(Integer.parseInt(now().toString().substring(10)+2),today(),addDay(today(),465),medConc,500) ;
        assertTrue(lotDAO.create(lot1));
        assertTrue(lotDAO.create(lot2));
        loadData();

        //verification que MedicamentConcentration est bien créé
        lstMedicConc = DAOFactory.getMedicamentConcentrationDAO().findAll(medicament);
        assertEquals(lstMedicConc.size(),1);


        //suppression de Medicament
        assertTrue(medDAO.delete(medicament));
        assertNull(medDAO.find(medicament.getId()));

        lstMedicConc = DAOFactory.getMedicamentConcentrationDAO().findAll(medicament);
        assertEquals(lstMedicConc.size(),0);

    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("unittest.TestMedicamentDAO");
    }
}
