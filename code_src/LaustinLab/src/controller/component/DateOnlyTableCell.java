package controller.component;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import java.sql.Date;
import java.time.LocalDate;

/**
 *     Author : Rochez Justin, Sapin Laurent
 *     Creation : 10/03/2021
 *     Modification : 17/03/2021
 *     Revision : 0.9
 *     Description : Classe destinée à creer une DatePicker dans un TableCell
 */
public class DateOnlyTableCell<S, T> extends TableCell<Object, Date> {

    private DatePicker datePicker;
    private Date item;

    /**
     * Constructeur
     */
    public DateOnlyTableCell() {
        super();
        if (datePicker == null) {
            createDatePicker();
        }
        setGraphic(datePicker);
        setContentDisplay(ContentDisplay.TEXT_ONLY);
    }

    @Override
    public void updateItem(Date item, boolean empty) {
        if (item != null){
            this.item = item;
            setText(item+"");
            super.updateItem(item, empty);
        }
    }

    /**
     * Méthode de création du DatePicker de la cellule
     * @return
     */
    private DatePicker createDatePicker() {
        this.datePicker = new DatePicker();
        datePicker.setEditable(true);
        datePicker.setOnAction(new EventHandler() {
            public void handle(Event t) {
                LocalDate date = datePicker.getValue();
                commitEdit(Date.valueOf(date));
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
            this.datePicker.setValue(item.toLocalDate());
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setContentDisplay(ContentDisplay.TEXT_ONLY);
    }


    public static <T> Callback<TableColumn<Object, Object>, TableCell<Object, Date>> forTableColumn() {
        return (TableColumn<Object, Object> tableColumn) -> new DateOnlyTableCell<Object, Date>();
    }

}