package gui;

import data.AkuSroubovak;
import data.BruskaVibracni;
import data.MichadloElektrickeRucni;
import data.Naradi;
import data.TypyNaradi;
import static data.TypyNaradi.AKU_SROUBOVAK;
import static data.TypyNaradi.BRUSKA_VIBRACNI;
import static data.TypyNaradi.MICHADLO_ELEKTRICKE_RUCNI;
import generator.GeneratorNaradi;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import kolekce.KolekceException;
import kolekce.SpojovySeznam;
import perzistence.PraceSeSoubory;

public class MyApp extends Application {

    private SpojovySeznam<Naradi> seznam; // Spojový seznam pro uchovávání nářadí.
    private ListView<Naradi> lv_naradi; // Seznam pro zobrazení nářadí v GUI.
    private int aktualniPozice = 0; // Uchovává aktuální pozici v seznamu.
    private ObservableList<Naradi> observableList; // Observable list pro propojení se seznamem.
    private PraceSeSoubory prace; // Třída pro práci se soubory.
    private Predicate<Naradi> filtr = t -> true; // Predikát pro filtraci nářadí.
    private boolean validnostVstupu = false; // Validita vstupních dat.
    private ComboBox<Naradi> cb_novy; // ComboBox pro výběr nového nářadí.

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        seznam = new SpojovySeznam(); // Vytvoření nového seznamu nářadí.
        prace = new PraceSeSoubory(); // Inicializace třídy pro práci se soubory.
        observableList = FXCollections.observableArrayList(seznam.toList());

        lv_naradi = new ListView<>(); // Vytvoření ListView pro nářadí.
        lv_naradi.setPrefWidth(620);
        lv_naradi.setPrefHeight(476);
 
        // Vytvoření hlavního okna
        stage.setTitle("Půjčovna nářadí - gui");
        AnchorPane root = new AnchorPane();
        Scene scene = new Scene(root, 720, 576);
        stage.setScene(scene);

        // Vytvoření kontejneru pro ovládací tlačítka.
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.TOP_CENTER);
        vbox.setLayoutX(620);
        vbox.setPrefHeight(476);
        vbox.setPrefWidth(100);

        Label lb_prochazeni = new Label();
        lb_prochazeni.setText("Procházení");
        vbox.setMargin(lb_prochazeni, new Insets(10, 0, 0, 0));

        // Tlačítko pro nastavení aktuálního prvku na první prvek v seznamu
        Button btn_prvni = new Button("První");
        vbox.setMargin(btn_prvni, new Insets(10, 10, 0, 10));
        btn_prvni.setPrefWidth(200);
        btn_prvni.setOnAction((t) -> {
            try {
                seznam.nastavPrvni();
                aktualniPozice = 0;
                lv_naradi.getSelectionModel().select(aktualniPozice);
            } catch (Exception e) {
                (new Alert(Alert.AlertType.ERROR, "Seznam je prázdný!", ButtonType.CLOSE)).show();
            }
        });

        // Tlačítko pro nastavení aktuálního prvku na další prvek v pořadí
        Button btn_dalsi = new Button("Další");
        vbox.setMargin(btn_dalsi, new Insets(10, 10, 0, 10));
        btn_dalsi.setPrefWidth(200);
        btn_dalsi.setOnAction((ActionEvent t) -> {
            try {
                seznam.dalsi();
                aktualniPozice++;
                lv_naradi.getSelectionModel().select(aktualniPozice);
            } catch (Exception e) {
                if (seznam.size() == 0) {
                    (new Alert(Alert.AlertType.ERROR, "Seznam je prázdný!", ButtonType.CLOSE)).show();
                    return;
                }
                try {
                    seznam.dejAktualni();
                } catch (Exception f) {
                    (new Alert(Alert.AlertType.ERROR, "Nebyl nastaven aktuální prvek!", ButtonType.CLOSE)).show();
                    return;
                }
                (new Alert(Alert.AlertType.ERROR, "Jste na konci seznamu!", ButtonType.CLOSE)).show();
            }
        });

        // Tlačítko pro nastavení aktuálního prvku na předchozí prvek seznamu
        Button btn_predchozi = new Button("Předchozí");
        vbox.setMargin(btn_predchozi, new Insets(10, 10, 0, 10));
        btn_predchozi.setPrefWidth(200);
        btn_predchozi.setOnAction((t) -> {
            try {
                seznam.predchozi();
                aktualniPozice--;
                lv_naradi.getSelectionModel().select(aktualniPozice);
            } catch (Exception e) {
                if (seznam.size() == 0) {
                    (new Alert(Alert.AlertType.ERROR, "Seznam je prázdný!", ButtonType.CLOSE)).show();
                    return;
                }
                try {
                    seznam.dejAktualni();
                } catch (Exception f) {
                    (new Alert(Alert.AlertType.ERROR, "Nebyl nastaven aktuální prvek!", ButtonType.CLOSE)).show();
                    return;
                }

                (new Alert(Alert.AlertType.ERROR, "Jste na začátku seznamu!", ButtonType.CLOSE)).show();
            }

        });

        // Tlačítko pro nastavení aktuálního prvku na poslední prvek v seznamu
        Button btn_posledni = new Button("Poslední");
        vbox.setMargin(btn_posledni, new Insets(10, 10, 0, 10));
        btn_posledni.setPrefWidth(200);
        btn_posledni.setOnAction((t) -> {
            try {
                seznam.nastavPosledni();
                aktualniPozice = seznam.size() - 1;
                lv_naradi.getSelectionModel().select(aktualniPozice);
            } catch (Exception e) {
                (new Alert(Alert.AlertType.ERROR, "Seznam je prázdný!", ButtonType.CLOSE)).show();
            }
        });

        Label lb_prikazy = new Label("Příkazy");

        vbox.setMargin(lb_prikazy,
                new Insets(10, 0, 0, 0));

        // Tlačítko na editaci jednotlivého nářadí
        Button btn_edituj = new Button("Edituj");
        vbox.setMargin(btn_edituj,
                new Insets(10, 10, 0, 10));
        btn_edituj.setPrefWidth(
                200);
        btn_edituj.setOnAction((t) -> {
            try {
                TypyNaradi typ = seznam.dejAktualni().getTyp();
                switch (typ) {
                    case AKU_SROUBOVAK:
                        Dialog<ButtonType> dialog = new Dialog<>();
                        dialog.setTitle("Úprava šroubováku");

                        ButtonType saveButtonType = new ButtonType("Uložit", ButtonData.OK_DONE);
                        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

                        GridPane gridPane = new GridPane();
                        gridPane.setHgap(10);
                        gridPane.setVgap(10);
                        gridPane.setPadding(new Insets(20, 150, 10, 10));

                        TextField tf_hmotnost = new TextField();
                        tf_hmotnost.setText(String.format(Locale.ENGLISH,"%.2f", seznam.dejAktualni().getHmotnost()));

                        TextField tf_pracovniNapeti = new TextField();
                        tf_pracovniNapeti.setText(String.format(Locale.ENGLISH, "%d", ((AkuSroubovak) seznam.dejAktualni()).getPracovniNapeti()));

                        Label lb_id = new Label(String.valueOf(seznam.dejAktualni().getID()));

                        gridPane.add(new Label("ID:"), 0, 0);
                        gridPane.add(lb_id, 1, 0);
                        gridPane.add(new Label("Hmotnost:"), 0, 1);
                        gridPane.add(tf_hmotnost, 1, 1);
                        gridPane.add(new Label("Pracovní napětí:"), 0, 2);
                        gridPane.add(tf_pracovniNapeti, 1, 2);

                        dialog.getDialogPane().setContent(gridPane);

                        dialog.setOnCloseRequest(event -> {
                            ButtonType buttonType = (ButtonType) dialog.getResult();
                            if (buttonType != saveButtonType) {
                                return;
                            }

                            String str = "";
                            double hmotnost = 0;
                            int pracovniNapeti = 0;
                            try {
                                hmotnost = Double.parseDouble(tf_hmotnost.getText());
                                if (hmotnost <= 0) {
                                    str += "Hmotnost musí být kladné číslo.";
                                }
                            } catch (Exception w) {
                                str += "Hmotnost musí být kladné desetinné číslo.\n";
                            }

                            try {
                                pracovniNapeti = Integer.parseInt(tf_pracovniNapeti.getText());
                                if (pracovniNapeti <= 0) {
                                    str += "Pracovní napětí musí být kladné číslo.";
                                }
                            } catch (Exception q) {
                                str += "Pracovní napětí musí být kladné celé číslo.\n";
                            }

                            if (str.length() != 0) {
                                Alert alert = new Alert(Alert.AlertType.ERROR, str, ButtonType.OK);
                                alert.showAndWait();
                                event.consume(); // zamezíme zavření dialogu
                            } else {
                                AkuSroubovak sroubovak = new AkuSroubovak(Integer.parseInt(lb_id.getText()), hmotnost, pracovniNapeti);
                                try {
                                    Naradi naradi = seznam.dejAktualni();
                                    seznam.vlozZaAktualni(sroubovak);
                                    seznam.odeberAktualni();
                                    seznam.nastavPrvni();
                                    while (seznam.dejAktualni() != sroubovak) {
                                        seznam.dalsi();
                                    }
                                    lv_naradi.getSelectionModel().select(sroubovak);
                                } catch (KolekceException ex) {

                                }

                            }
                        });

                        Optional<ButtonType> result = dialog.showAndWait();
                        break;
                    case BRUSKA_VIBRACNI:
                        Dialog<ButtonType> dialogBruska = new Dialog<>();
                        dialogBruska.setTitle("Úprava brusky");

                        ButtonType saveButtonTypeBruska = new ButtonType("Uložit", ButtonData.OK_DONE);
                        dialogBruska.getDialogPane().getButtonTypes().addAll(saveButtonTypeBruska, ButtonType.CANCEL);

                        GridPane gridPaneBruska = new GridPane();
                        gridPaneBruska.setHgap(10);
                        gridPaneBruska.setVgap(10);
                        gridPaneBruska.setPadding(new Insets(20, 150, 10, 10));

                        TextField tf_hmotnostBruska = new TextField();
                        tf_hmotnostBruska.setText(String.format(Locale.ENGLISH, "%.2f", seznam.dejAktualni().getHmotnost()));

                        TextField tf_vykon = new TextField();
                        tf_vykon.setText(String.format(Locale.ENGLISH, "%.2f", ((BruskaVibracni)seznam.dejAktualni()).getVykon()));

                        Label lb_idBruska = new Label(String.valueOf(seznam.dejAktualni().getID()));

                        gridPaneBruska.add(new Label("ID:"), 0, 0);
                        gridPaneBruska.add(lb_idBruska, 1, 0);
                        gridPaneBruska.add(new Label("Hmotnost:"), 0, 1);
                        gridPaneBruska.add(tf_hmotnostBruska, 1, 1);
                        gridPaneBruska.add(new Label("Výkon:"), 0, 2);
                        gridPaneBruska.add(tf_vykon, 1, 2);

                        dialogBruska.getDialogPane().setContent(gridPaneBruska);

                        dialogBruska.setOnCloseRequest(event -> {
                            ButtonType buttonType = (ButtonType) dialogBruska.getResult();
                            if (buttonType != saveButtonTypeBruska) {
                                return;
                            }

                            String str = "";
                            double hmotnost = 0;
                            double vykon = 0;
                            try {
                                hmotnost = Double.parseDouble(tf_hmotnostBruska.getText());
                                if (hmotnost <= 0) {
                                    str += "Hmotnost musí být kladné číslo.";
                                }
                            } catch (Exception w) {
                                str += "Hmotnost musí být kladné desetinné číslo.\n";
                            }

                            try {
                                vykon = Double.parseDouble(tf_vykon.getText());
                                if (vykon <= 0) {
                                    str += "Výkon musí být kladné číslo.";
                                }
                            } catch (Exception q) {
                                str += "Výkon musí být kladné desetinné číslo.\n";
                            }

                            if (str.length() != 0) {
                                Alert alert = new Alert(Alert.AlertType.ERROR, str, ButtonType.OK);
                                alert.showAndWait();
                                event.consume(); // zamezíme zavření dialogu
                            } else {
                                BruskaVibracni bruska = new BruskaVibracni(Integer.parseInt(lb_idBruska.getText()),hmotnost, vykon);
                              
                                try {
                                    Naradi naradi = seznam.dejAktualni();
                                    seznam.vlozZaAktualni(bruska);
                                    seznam.odeberAktualni();
                                    seznam.nastavPrvni();
                                    while (seznam.dejAktualni() != bruska) {
                                        seznam.dalsi();
                                    }
                                    lv_naradi.getSelectionModel().select(bruska);
                                } catch (KolekceException ex) {

                                }
                            }
                        });

                        Optional<ButtonType> resultBruska = dialogBruska.showAndWait();
                        break;
                    default:
                        Dialog<ButtonType> dialogMichadlo = new Dialog<>();
                        dialogMichadlo.setTitle("Úprava míchadla");

                        ButtonType saveButtonTypeMichadlo = new ButtonType("Uložit", ButtonData.OK_DONE);
                        dialogMichadlo.getDialogPane().getButtonTypes().addAll(saveButtonTypeMichadlo, ButtonType.CANCEL);

                        GridPane gridPaneMichadlo = new GridPane();
                        gridPaneMichadlo.setHgap(10);
                        gridPaneMichadlo.setVgap(10);
                        gridPaneMichadlo.setPadding(new Insets(20, 150, 10, 10));

                        TextField tf_hmotnostMichadlo = new TextField();
                        tf_hmotnostMichadlo.setText(String.format(Locale.ENGLISH, "%.2f", seznam.dejAktualni().getHmotnost()));

                        TextField tf_maximalniOtacky = new TextField();
                        tf_maximalniOtacky.setText(String.format(Locale.ENGLISH, "%.2f", ((MichadloElektrickeRucni)seznam.dejAktualni()).getMaximalniOtacky()));

                        Label lb_idMichadlo = new Label(String.valueOf(seznam.dejAktualni().getID()));

                        gridPaneMichadlo.add(new Label("ID:"), 0, 0);
                        gridPaneMichadlo.add(lb_idMichadlo, 1, 0);
                        gridPaneMichadlo.add(new Label("Hmotnost:"), 0, 1);
                        gridPaneMichadlo.add(tf_hmotnostMichadlo, 1, 1);
                        gridPaneMichadlo.add(new Label("Maximální otáčky:"), 0, 2);
                        gridPaneMichadlo.add(tf_maximalniOtacky, 1, 2);

                        dialogMichadlo.getDialogPane().setContent(gridPaneMichadlo);

                        dialogMichadlo.setOnCloseRequest(event -> {
                            ButtonType buttonType = (ButtonType) dialogMichadlo.getResult();
                            if (buttonType != saveButtonTypeMichadlo) {
                                return;
                            }

                            String str = "";
                            double hmotnost = 0;
                            double maximalniOtacky = 0;
                            try {
                                hmotnost = Double.parseDouble(tf_hmotnostMichadlo.getText());
                                if (hmotnost <= 0) {
                                    str += "Hmotnost musí být kladné číslo.";
                                }
                            } catch (Exception w) {
                                str += "Hmotnost musí být kladné desetinné číslo.\n";
                            }

                            try {
                                maximalniOtacky = Double.parseDouble(tf_maximalniOtacky.getText());
                                if (maximalniOtacky <= 0) {
                                    str += "Maximální otáčky musí být kladné číslo.";
                                }
                            } catch (Exception q) {
                                str += "Maximální otáčky musí být kladné desetinné číslo.\n";
                            }

                            if (str.length() != 0) {
                                Alert alert = new Alert(Alert.AlertType.ERROR, str, ButtonType.OK);
                                alert.showAndWait();
                                event.consume(); // zamezíme zavření dialogu
                            } else {
                                MichadloElektrickeRucni michadlo = new MichadloElektrickeRucni(Integer.parseInt(lb_idMichadlo.getText()),hmotnost, maximalniOtacky);
                                
                                try {
                                    Naradi naradi = seznam.dejAktualni();
                                    seznam.vlozZaAktualni(michadlo);
                                    seznam.odeberAktualni();
                                    seznam.nastavPrvni();
                                    while (seznam.dejAktualni() != michadlo) {
                                        seznam.dalsi();
                                    }
                                    lv_naradi.getSelectionModel().select(michadlo);
                                } catch (KolekceException ex) {

                                }
                            }
                        });

                        Optional<ButtonType> resultMichadlo = dialogMichadlo.showAndWait();
                        break;
                }

                observableList = FXCollections.observableArrayList(seznam.toList());
                lv_naradi.setItems(observableList);
            } catch (Exception e) {
                (new Alert(Alert.AlertType.ERROR, "Nebyl nastaven aktuální prvek!", ButtonType.CLOSE)).show();
            }
        });

        // Tlačítko pro odebrání aktuálního prvku ze seznamu
        Button btn_vyjmi = new Button("Vyjmi");
        vbox.setMargin(btn_vyjmi,
                new Insets(10, 10, 0, 10));
        btn_vyjmi.setPrefWidth(
                200);
        btn_vyjmi.setOnAction((t) -> {
            try {
                seznam.dejAktualni();
                seznam.odeberAktualni();
                observableList = FXCollections.observableArrayList(seznam.toList());
                lv_naradi.setItems(observableList);
            } catch (Exception e) {
                (new Alert(Alert.AlertType.ERROR, "Nebyl nastaven aktuální prvek!", ButtonType.CLOSE)).show();
            }
        });

        // Tlačítko pro zobrazení nářadí
        Button btn_zobraz = new Button("Zobraz");
        vbox.setMargin(btn_zobraz,
                new Insets(10, 10, 0, 10));
        btn_zobraz.setPrefWidth(
                200);
        btn_zobraz.setOnAction((t) -> {
            observableList = FXCollections.observableArrayList(seznam.toList());
            lv_naradi.setItems(observableList);
        });

        // Tlačítko pro vymazání nářadí
        Button btn_clear = new Button("Clear");
        vbox.setMargin(btn_clear,
                new Insets(10, 10, 0, 10));
        btn_clear.setPrefWidth(
                200);
        btn_clear.setOnAction((t) -> {
            observableList = FXCollections.observableArrayList();
            lv_naradi.setItems(observableList);

        });

        // Přidání jednotlivých komponent do kontejneru
        vbox.getChildren()
                .addAll(lb_prochazeni, btn_prvni, btn_dalsi, btn_predchozi, btn_posledni, lb_prikazy, btn_edituj, btn_vyjmi, btn_zobraz, btn_clear);

        // Tvorba horizontálního kontejneru
        HBox hbox = new HBox();

        hbox.setLayoutY(
                476);
        hbox.setPrefWidth(
                720);
        hbox.setPrefHeight(
                100);
        hbox.setAlignment(Pos.CENTER_LEFT);

        // Tlačítko pro generování nářadí
        Button btn_generuj = new Button("Generuj");
        hbox.setMargin(btn_generuj,
                new Insets(0, 0, 0, 10));
        btn_generuj.setOnAction(
                (t) -> {
                    Dialog<Integer> dialog = new Dialog<>();
                    dialog.setTitle("Vyberte číslo");

                    Spinner<Integer> spinner = new Spinner<>(1, Integer.MAX_VALUE, 1, 1);
                    spinner.setEditable(false);
                    spinner.setPrefWidth(80);
                    SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory
                    = (SpinnerValueFactory.IntegerSpinnerValueFactory) spinner.getValueFactory();
                    valueFactory.setConverter(new StringConverter<Integer>() {
                        @Override
                        public String toString(Integer value) {
                            return value.toString();
                        }

                        @Override
                        public Integer fromString(String value) {
                            return Integer.parseInt(value);
                        }
                    });

                    dialog.getDialogPane().setContent(spinner);

                    ButtonType okButtonType = new ButtonType("OK", ButtonData.OK_DONE);
                    dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

                    dialog.setResultConverter(dialogButton -> {
                        if (dialogButton == okButtonType) {
                            return spinner.getValue();
                        }
                        return null;
                    });

                    Optional<Integer> result = dialog.showAndWait();
                    result.ifPresent(selectedNumber -> {
                        System.out.println("Bylo vygenerováno: " + selectedNumber + " prvků.");

                        seznam.zrus();

                        GeneratorNaradi generovani = new GeneratorNaradi();
                        for (int i = 0; i < selectedNumber; i++) {
                            seznam.vlozPosledni(generovani.generuj());
                        }

                        ObservableList<Naradi> observableList = FXCollections.observableArrayList(seznam.toList());
                        lv_naradi.setItems(observableList);
                    });
                }
        );

        // Tlačítko pro uložení nářadí do textového souboru
        Button btn_uloz = new Button("Ulož");
        hbox.setMargin(btn_uloz,
                new Insets(0, 0, 0, 10));
        btn_uloz.setOnAction((t) -> {
            try {
                prace.ulozTxt(seznam, "zaloha.txt");
                seznam.zrus();
                observableList = FXCollections.observableArrayList(seznam.toList());
                lv_naradi.setItems(observableList);
            } catch (Exception e) {
                System.out.println("Nepodařilo se uložit textovou zálohu.");
            }
        });

        // Tlačítko pro načtení nářadí z textového souboru
        Button btn_nacti = new Button("Načti");
        hbox.setMargin(btn_nacti,
                new Insets(0, 0, 0, 10));
        btn_nacti.setOnAction((t) -> {
            try {
                seznam = prace.nactiTxt("zaloha.txt");
                observableList = FXCollections.observableArrayList(seznam.toList());
                lv_naradi.setItems(observableList);
            } catch (Exception e) {
                System.out.println("Nepodařilo se načíst textovou zálohu.");
            }
        });

        // Combobox pro výběr typu nářadí
        cb_novy = new ComboBox(FXCollections.observableList(Arrays.asList(TypyNaradi.values())));
        hbox.setMargin(cb_novy,
                new Insets(0, 0, 0, 10));
        cb_novy.setPromptText("Nový");
        cb_novy.setPrefWidth(82);

        cb_novy.setOnAction((e) -> {
            TypyNaradi typ;

            typ = ((ComboBox<TypyNaradi>) e.getSource()).getValue();

            if (typ == null) {
                return;
            }
            switch (typ) {
                case AKU_SROUBOVAK:
                    Dialog<ButtonType> dialog = new Dialog<>();
                    dialog.setTitle("Přidání šroubováku");

                    ButtonType saveButtonType = new ButtonType("Uložit", ButtonData.OK_DONE);
                    dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

                    GridPane gridPane = new GridPane();
                    gridPane.setHgap(10);
                    gridPane.setVgap(10);
                    gridPane.setPadding(new Insets(20, 150, 10, 10));

                    TextField tf_hmotnost = new TextField();
                    tf_hmotnost.setPromptText("Hmotnost");

                    TextField tf_pracovniNapeti = new TextField();
                    tf_pracovniNapeti.setPromptText("Pracovní napětí");

                    Label lb_id = new Label(String.valueOf(Naradi.dalsiId));

                    gridPane.add(new Label("ID:"), 0, 0);
                    gridPane.add(lb_id, 1, 0);
                    gridPane.add(new Label("Hmotnost:"), 0, 1);
                    gridPane.add(tf_hmotnost, 1, 1);
                    gridPane.add(new Label("Pracovní napětí:"), 0, 2);
                    gridPane.add(tf_pracovniNapeti, 1, 2);

                    dialog.getDialogPane().setContent(gridPane);

                    dialog.setOnCloseRequest(event -> {
                        ButtonType buttonType = (ButtonType) dialog.getResult();
                        if (buttonType != saveButtonType) {
                            return;
                        }

                        String str = "";
                        double hmotnost = 0;
                        int pracovniNapeti = 0;
                        try {
                            hmotnost = Double.parseDouble(tf_hmotnost.getText());
                            if (hmotnost <= 0) {
                                str += "Hmotnost musí být kladné číslo.";
                            }
                        } catch (Exception w) {
                            str += "Hmotnost musí být kladné desetinné číslo.\n";
                        }

                        try {
                            pracovniNapeti = Integer.parseInt(tf_pracovniNapeti.getText());
                            if (pracovniNapeti <= 0) {
                                str += "Pracovní napětí musí být kladné číslo.";
                            }
                        } catch (Exception q) {
                            str += "Pracovní napětí musí být kladné celé číslo.\n";
                        }

                        if (str.length() != 0) {
                            Alert alert = new Alert(Alert.AlertType.ERROR, str, ButtonType.OK);
                            alert.showAndWait();
                            event.consume(); // zamezíme zavření dialogu
                        } else {
                            AkuSroubovak sroubovak = new AkuSroubovak(hmotnost, pracovniNapeti);
                            seznam.vlozPosledni(sroubovak);

                        }
                    });

                    Optional<ButtonType> result = dialog.showAndWait();

                    break;
                case BRUSKA_VIBRACNI:
                    Dialog<ButtonType> dialogBruska = new Dialog<>();
                    dialogBruska.setTitle("Přidání brusky");

                    ButtonType saveButtonTypeBruska = new ButtonType("Uložit", ButtonData.OK_DONE);
                    dialogBruska.getDialogPane().getButtonTypes().addAll(saveButtonTypeBruska, ButtonType.CANCEL);

                    GridPane gridPaneBruska = new GridPane();
                    gridPaneBruska.setHgap(10);
                    gridPaneBruska.setVgap(10);
                    gridPaneBruska.setPadding(new Insets(20, 150, 10, 10));

                    TextField tf_hmotnostBruska = new TextField();
                    tf_hmotnostBruska.setPromptText("Zadejte hmotnost:");

                    TextField tf_vykon = new TextField();
                    tf_vykon.setPromptText("Zadejte výkon:");

                    Label lb_idBruska = new Label(String.valueOf(Naradi.dalsiId));

                    gridPaneBruska.add(new Label("ID:"), 0, 0);
                    gridPaneBruska.add(lb_idBruska, 1, 0);
                    gridPaneBruska.add(new Label("Hmotnost:"), 0, 1);
                    gridPaneBruska.add(tf_hmotnostBruska, 1, 1);
                    gridPaneBruska.add(new Label("Výkon:"), 0, 2);
                    gridPaneBruska.add(tf_vykon, 1, 2);

                    dialogBruska.getDialogPane().setContent(gridPaneBruska);

                    dialogBruska.setOnCloseRequest(event -> {
                        ButtonType buttonType = (ButtonType) dialogBruska.getResult();
                        if (buttonType != saveButtonTypeBruska) {
                            return;
                        }

                        String str = "";
                        double hmotnost = 0;
                        double vykon = 0;
                        try {
                            hmotnost = Double.parseDouble(tf_hmotnostBruska.getText());
                            if (hmotnost <= 0) {
                                str += "Hmotnost musí být kladné číslo.";
                            }
                        } catch (Exception w) {
                            str += "Hmotnost musí být kladné desetinné číslo.\n";
                        }

                        try {
                            vykon = Double.parseDouble(tf_vykon.getText());
                            if (vykon <= 0) {
                                str += "Výkon musí být kladné číslo.";
                            }
                        } catch (Exception q) {
                            str += "Výkon musí být kladné desetinné číslo.\n";
                        }

                        if (str.length() != 0) {
                            Alert alert = new Alert(Alert.AlertType.ERROR, str, ButtonType.OK);
                            alert.showAndWait();
                            event.consume(); // zamezíme zavření dialogu
                        } else {
                            BruskaVibracni bruska = new BruskaVibracni(hmotnost, vykon);
                            seznam.vlozPosledni(bruska);
                        }
                    });

                    Optional<ButtonType> resultBruska = dialogBruska.showAndWait();
                    break;
                case MICHADLO_ELEKTRICKE_RUCNI:
                    Dialog<ButtonType> dialogMichadlo = new Dialog<>();
                    dialogMichadlo.setTitle("Přidání míchadla");

                    ButtonType saveButtonTypeMichadlo = new ButtonType("Uložit", ButtonData.OK_DONE);
                    dialogMichadlo.getDialogPane().getButtonTypes().addAll(saveButtonTypeMichadlo, ButtonType.CANCEL);

                    GridPane gridPaneMichadlo = new GridPane();
                    gridPaneMichadlo.setHgap(10);
                    gridPaneMichadlo.setVgap(10);
                    gridPaneMichadlo.setPadding(new Insets(20, 150, 10, 10));

                    TextField tf_hmotnostMichadlo = new TextField();
                    tf_hmotnostMichadlo.setPromptText("Zadejte hmotnost:");

                    TextField tf_maximalniOtacky = new TextField();
                    tf_maximalniOtacky.setPromptText("Zadejte maximální otáčky:");

                    Label lb_idMichadlo = new Label(String.valueOf(Naradi.dalsiId));

                    gridPaneMichadlo.add(new Label("ID:"), 0, 0);
                    gridPaneMichadlo.add(lb_idMichadlo, 1, 0);
                    gridPaneMichadlo.add(new Label("Hmotnost:"), 0, 1);
                    gridPaneMichadlo.add(tf_hmotnostMichadlo, 1, 1);
                    gridPaneMichadlo.add(new Label("Maximální otáčky:"), 0, 2);
                    gridPaneMichadlo.add(tf_maximalniOtacky, 1, 2);

                    dialogMichadlo.getDialogPane().setContent(gridPaneMichadlo);

                    dialogMichadlo.setOnCloseRequest(event -> {
                        ButtonType buttonType = (ButtonType) dialogMichadlo.getResult();
                        if (buttonType != saveButtonTypeMichadlo) {
                            return;
                        }

                        String str = "";
                        double hmotnost = 0;
                        double maximalniOtacky = 0;
                        try {
                            hmotnost = Double.parseDouble(tf_hmotnostMichadlo.getText());
                            if (hmotnost <= 0) {
                                str += "Hmotnost musí být kladné číslo.";
                            }
                        } catch (Exception w) {
                            str += "Hmotnost musí být kladné desetinné číslo.\n";
                        }

                        try {
                            maximalniOtacky = Double.parseDouble(tf_maximalniOtacky.getText());
                            if (maximalniOtacky <= 0) {
                                str += "Maximální otáčky musí být kladné číslo.";
                            }
                        } catch (Exception q) {
                            str += "Maximální otáčky musí být kladné desetinné číslo.\n";
                        }

                        if (str.length() != 0) {
                            Alert alert = new Alert(Alert.AlertType.ERROR, str, ButtonType.OK);
                            alert.showAndWait();
                            event.consume(); // zamezíme zavření dialogu
                        } else {
                            MichadloElektrickeRucni michadlo = new MichadloElektrickeRucni(hmotnost, maximalniOtacky);
                            seznam.vlozPosledni(michadlo);
                        }
                    });

                    Optional<ButtonType> resultMichadlo = dialogMichadlo.showAndWait();

                    break;
                default:
                    break;
            }

            obnovZobrazeniSeznamu();
            Platform.runLater(() -> cb_novy.getSelectionModel().clearSelection());

        });

        // Combobox pro filtrování jednotlivých typů nářadí
        ComboBox<TypyNaradi> cb_filtr = new ComboBox(FXCollections.observableList(Arrays.asList(TypyNaradi.values())));
        cb_filtr.setPromptText("Filtr");
        cb_filtr.setPrefWidth(65);
        hbox.setMargin(cb_filtr,
                new Insets(0, 0, 0, 10));
        cb_filtr.setOnAction((e) -> {
            TypyNaradi typ;
            typ = ((ComboBox<TypyNaradi>) e.getSource()).getValue();
            if (typ == null) {
                return;
            }
            switch (typ) {
                case AKU_SROUBOVAK:
                    filtr = t -> t.getTyp() == TypyNaradi.AKU_SROUBOVAK;
                    break;
                case BRUSKA_VIBRACNI:
                    filtr = t -> t.getTyp() == TypyNaradi.BRUSKA_VIBRACNI;
                    break;
                case MICHADLO_ELEKTRICKE_RUCNI:
                    filtr = t -> t.getTyp() == TypyNaradi.MICHADLO_ELEKTRICKE_RUCNI;
                    break;
                default:
                    filtr = t -> false;
                    break;
            }
            obnovZobrazeniSeznamu();
        });

        // Tlačítko pro resetování filteru
        Button btn_vycistitFiltr = new Button("Reset");
        hbox.setMargin(btn_vycistitFiltr,
                new Insets(0, 0, 0, 10));
        btn_vycistitFiltr.setOnAction((t) -> {
            observableList = FXCollections.observableArrayList(seznam.toList());
            lv_naradi.setItems(observableList);
            cb_filtr.getSelectionModel().clearSelection();
            filtr = e -> true;
        });

        Label lb_id = new Label("ID:");

        hbox.setMargin(lb_id,
                new Insets(0, 0, 0, 10));

        TextField tf_id = new TextField();

        tf_id.setPrefWidth(
                30);
        hbox.setMargin(tf_id,
                new Insets(0, 0, 0, 10));

        // Tlačítko pro vyhledání nářadí na základě zadaného ID
        Button btn_najdi = new Button("Najdi");
        hbox.setMargin(btn_najdi,
                new Insets(0, 0, 0, 10));
        btn_najdi.setOnAction((t) -> {
            try {
                int aktualniPozice = 0;
                int hledaneId = Integer.parseInt(tf_id.getText());
                seznam.nastavPrvni();
                try {
                    while (seznam.dejAktualni().getID() != hledaneId) {
                        seznam.dalsi();
                    }
                    lv_naradi.getSelectionModel().select(aktualniPozice);
                    while (lv_naradi.getSelectionModel().getSelectedItem() != seznam.dejAktualni()) {
                        aktualniPozice++;
                        lv_naradi.getSelectionModel().select(aktualniPozice);
                    }
                    this.aktualniPozice = aktualniPozice;
                } catch (Exception e) {
                    (new Alert(Alert.AlertType.ERROR, "Nebyl nalezen žádný prvek!", ButtonType.CLOSE)).show();
                }
            } catch (Exception e) {
                (new Alert(Alert.AlertType.ERROR, "Nebyl nalezen žádný prvek!", ButtonType.CLOSE)).show();
            }
        });

        // Tlačítko pro uložení seznamu nářadí do binárního souboru
        Button btn_zalohuj = new Button("Zálohuj");
        hbox.setMargin(btn_zalohuj,
                new Insets(0, 0, 0, 10));
        btn_zalohuj.setOnAction((t) -> {
            try {
                prace.ulozBinarni(seznam, "zaloha.bin");
                seznam.zrus();
                observableList = FXCollections.observableArrayList(seznam.toList());
                lv_naradi.setItems(observableList);
            } catch (Exception e) {
                System.out.println("Záloha seznamu se nezdařila.");
            }
        });

        // Tlačítko pro načtení seznamu nářadí z binárního souboru
        Button btn_obnov = new Button("Obnov");
        hbox.setMargin(btn_obnov,
                new Insets(0, 0, 0, 10));
        btn_obnov.setOnAction((t) -> {
            try {
                seznam = prace.nactiBinarni("zaloha.bin");
                observableList = FXCollections.observableArrayList(seznam.toList());
                lv_naradi.setItems(observableList);
            } catch (Exception e) {
                System.out.println("Obnova seznamu se nezdařila.");
            }
        });

        // Tlačítko pro zrušení seznamu nářadí
        Button btn_zrus = new Button("Zruš");
        hbox.setMargin(btn_zrus,
                new Insets(0, 10, 0, 10));
        btn_zrus.setOnAction((t) -> {
            seznam.zrus();
            observableList = FXCollections.observableArrayList(seznam.toList());
            lv_naradi.setItems(observableList);
        });

        // Přidání jednotlivých komponent do horizontálního kontejneru
        hbox.getChildren()
                .addAll(btn_generuj, btn_uloz, btn_nacti, cb_novy, cb_filtr, btn_vycistitFiltr, lb_id, tf_id, btn_najdi, btn_zalohuj, btn_obnov, btn_zrus);

        root.getChildren()
                .addAll(vbox, hbox, lv_naradi);

        stage.setResizable(
                false);
        stage.show();
    }

    // Aktualizace zobrazení seznamu nářadí
    private void obnovZobrazeniSeznamu() {
        observableList.clear();
        seznam.stream()
                .filter(filtr)
                .forEach(t -> observableList.add(t));
        lv_naradi.setItems(observableList);
    }
}
