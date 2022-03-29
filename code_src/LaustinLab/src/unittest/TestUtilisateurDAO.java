package unittest;
/**
 * Author : Rochez Justin, Sapin Laurent
 *     Creation : 21/02/2021
 *     Modification : 22/02/2021
 *     Revision : 1.0
 *     Description : Class de Test unitaire pour UtilisateurDAO
 */
import DAO.DAOFactory;
import DAO.UtilisateurDAO;
import exceptions.ConstraintException;
import exceptions.DBException;
import model.Facture;
import model.LigneFacture;
import model.Utilisateur;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import utility.Utility;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import static org.junit.Assert.*;
import static utility.Utility.Now;

public class TestUtilisateurDAO {
    private static UtilisateurDAO uDAO;
    private ArrayList<Utilisateur> lstUser;
    private Utilisateur user;
    /**
     * methode executée une fois avant toutes les autres méthodes.
     * Cette méthode va contruire le DAO.
     */
    @BeforeClass
    public static void initialiser() throws DBException {
        uDAO = DAOFactory.getUtilisateurDAO();
    }
    /**
     * Methode loadData, executée avant chaque méthode
     * Cette méthode va mettre à jour l'objet afin que cette objet soit celui avec l'id le plus grand.
     * Cette méthode permettra donc d'avoir un objet de test toujours à jour
     * @throws DBException
     */
    @Before
    public void loadData() throws DBException {
        lstUser= uDAO.findAll();
        user = lstUser.get(lstUser.size()-1);
    }
    /**
     * Methode de test pour la création de l'objet dans la base de donnée.
     * cette méthode vérifie si le create retourne bien true à la création et ensuite verifira
     * la correspondance d'un attribut de l'objet (mis à jour)
     * @throws DBException
     */
    @Test
    public void acreate() throws NoSuchAlgorithmException, DBException, ConstraintException {
        System.out.println("create");
        Utilisateur u1 = new Utilisateur(-1,"test1","prenom","usertesteur", Utility.getHash("Password"));
        assertTrue(uDAO.create(u1));
        loadData();
        assertEquals(user.getLogin(),"usertesteur");
        //creation d'une facture associée
        Facture f = new Facture(-1,null, user, Now(),new ArrayList<LigneFacture>(),99.0,DAOFactory.getClientDAO().find(1));
        assertTrue(DAOFactory.getFactureDAO().create(f));
    }

    /**
     * Methode de verification du Mot de passe en hashé
     * @throws NoSuchAlgorithmException
     */
    @Test
    public void ccheckHashPassword() throws NoSuchAlgorithmException {
        System.out.println("check hash Password");
        assertEquals(user.getPassword(), Utility.getHash("Password"));
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
        assertEquals(user.getLogin(), "usertesteur");
        user.setLogin("new login");
        assertTrue(uDAO.update(user));
        loadData();
        assertEquals(user.getLogin(),"new login");
    }
    /**
     * Methode de test de suppression de la base de donnée
     * avec injection d'une contrainte d'intégrité pour verifier que celle ci est gérée
     * @throws DBException
     * @throws ConstraintException
     */
    @Test
    public void ddelete() throws DBException, ConstraintException {
        System.out.println("delete");
        assertTrue(uDAO.delete(user));
        assertNull(uDAO.find(user.getId()));
        //verification que les Facture ne sont plus associée à l'utilisateur
        assertEquals(DAOFactory.getFactureDAO().findAll(user).size(),0);

    }
    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("unittest.TestUtilisateurDAO");
    }
}
