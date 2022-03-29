package unittest;

import DAO.ClientDAO;
import DAO.DAOFactory;
import exceptions.ConstraintException;
import exceptions.DBException;
import model.Client;
import model.Facture;
import model.LigneFacture;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static utility.Utility.Now;
/**
 * Author : Rochez Justin, Sapin Laurent
 *     Creation : 21/02/2021
 *     Modification : 22/02/2021
 *     Revision : 0.7
 *     Description : Class de Test unitaire pour ClientDAO
 */
public class TestClientDAO {
    private static ClientDAO cDAO;
    private ArrayList<Client> lstClient;
    private Client client;

    /**
     * methode executée une fois avant toutes les autres méthodes.
     * Cette méthode va contruire le DAO.
     */
    @BeforeClass
    public static void initialiser(){
        cDAO = DAOFactory.getClientDAO();
    }

    /**
     * Methode loadData, executée avant chaque méthode
     * Cette méthode va mettre à jour l'objet afin que cette objet soit celui avec l'id le plus grand.
     * Cette méthode permettra donc d'avoir un objet de test toujours à jour
     * @throws DBException
     */
    @Before
    public void loadData() throws DBException {
        lstClient= cDAO.findAll();
        client = lstClient.get(lstClient.size()-1);
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
        Client client = new Client(-1,"TestNom","TestPrenom","superDenom","BE0505050505050505","rue du Test","15",
                DAOFactory.getVilleDAO().find(9),"0033252525","mymailtest@test.be");
        assertTrue(cDAO.create(client));
        loadData();
        assertEquals(client.getDenomination(), "superDenom");
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
        assertEquals(client.getDenomination(), "superDenom");
        client.setDenomination("HyperMegaDenomination");
        assertTrue(cDAO.update(client));
        loadData();
        assertEquals("HyperMegaDenomination", client.getDenomination());
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
        //creation d'une Facture pour l'associer au client
        Facture f = new Facture(-1,client,null,Now(),new ArrayList<LigneFacture>(),45.0, DAOFactory.getClientDAO().find(1));
        assertTrue(DAOFactory.getFactureDAO().create(f));
        ArrayList<Facture> lstFact =DAOFactory.getFactureDAO().findAll(client);
        assertNotEquals(0, lstFact.size());//different de zero pour prouver qu'il y a bien au moins une facture associée au client
        //suppression du client
        assertTrue(cDAO.delete(client));
        assertNull(cDAO.find(client.getId()));
        // verification que la Facture renvoi null pour ce client.
        assertNull( DAOFactory.getFactureDAO().find( lstFact.get(lstFact.size()-1).getId() ).getClient() );
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("unittest.TestClientDAO");
    }
}
