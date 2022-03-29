package controller.component;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Calendar;

/**
 *     Author : Rochez Justin, Sapin Laurent
 *     Creation : 10/03/2021
 *     Modification : 17/03/2021
 *     Revision : 0.9
 *     Description : Classe destinée à creer une DatePicker dans un TableCell Ce DatePicker créée gère les valeur TimeStamp
 */
public class DateTableCell<S, T> extends TableCell<Object, Timestamp> {

    private DatePicker datePicker;
    private Timestamp item;

    /**
     * Constructeur
     */
    public DateTableCell() {
        super();
        if (datePicker == null) {
            createDatePicker();
        }
        setGraphic(datePicker);
        setContentDisplay(ContentDisplay.TEXT_ONLY);
    }

    @Override
    public void updateItem(Timestamp item, boolean empty) {
        if (item != null){
            this.item = item;
            setText(item+"");
            super.updateItem(item, empty);
        }

    }

    /**
     * méthode Création du DatePicket à partir du TimeStamp de la cellule
     * @return
     */
    private DatePicker createDatePicker() {
        this.datePicker = new DatePicker();
        datePicker.setEditable(true);
        datePicker.setOnAction(new EventHandler() {
            public void handle(Event t) {
                LocalDate date = datePicker.getValue();
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.DAY_OF_MONTH, date.getDayOfMonth());
                cal.set(Calendar.MONTH, date.getMonthValue() - 1);
                cal.set(Calendar.YEAR, date.getYear());
                Timestamp timestamp = new Timestamp(cal.getTime().getTime());
                setText(timestamp.toString());
                commitEdit(timestamp);
                setContentDisplay(ContentDisplay.TEXT_ONLY);
            }
        });
        return this.datePicker;
    }

    @Override
    public void startEdit() {
        if (this.isEditable() && this.getTableView().isEditable() && this.getTableColumn().isEditable()) {
            if (this.datePicker == null) {
                this.datePicker = createDatePicker();
                this.datePicker.editableProperty().bind(this.editableProperty());
            }
            super.startEdit();
            this.datePicker.setValue(LocalDate.of(item.getYear()+1900,item.getMonth(),item.getDay()));
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setContentDisplay(ContentDisplay.TEXT_ONLY);
    }

    @Override
    public void commitEdit(Timestamp timestamp) {
        super.commitEdit(timestamp);
    }

    public static <T> Callback<TableColumn<Object, Object>, TableCell<Object, Timestamp>> forTableColumn() {
        return (TableColumn<Object, Object> tableColumn) -> new DateTableCell<Object, Timestamp>();
    }

}