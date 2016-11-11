package javafxapplication2;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import static javafx.scene.layout.Region.USE_PREF_SIZE;
import javafxapplication2.model.Content;
import javafxapplication2.model.User;

/**
 * FXML Controller class
 *
 * @author Sindalf
 */
public class FrontPageController implements Initializable {

    Main application;
    User user;
    Map cookies;
    Web web;

    @FXML
    private Button indexBtn;
    @FXML
    private Button searchBtn;
    @FXML
    private Button btnNext;
    @FXML
    private Button btnPrevious;

    @FXML
    private TextField searchBar;
    @FXML
    private TextField textPage;

    @FXML
    private Label labelPage;

    @FXML
    private TextArea testText;

    @FXML
    private FlowPane tagbox;

    @FXML
    private ImageView sampleImage;
    @FXML
    private ImageView sampleImagetwo;

    @FXML
    private ListView<String> imageList;
    @FXML
    private ListView<String> tagList;

    @FXML
    private HBox pageHBox;

    @FXML
    private AnchorPane tagListParent;
    @FXML
    int page = 1;

    Content[] content;

    public void setApp(Main application) {
        this.application = application;
        user = application.user;
        cookies = user.getCookies();
        web = new Web();
        web.setCookies(cookies);
    }

    public void getFrontPage() {
        try {
            Content[] c = web.index();
            if (displayContent(c, 1)) {
                pageHBox.setDisable(true);
            }
        } catch (Exception ex) {
            Logger.getLogger(FrontPageController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void searchTerm() {
        try {
            String[] terms = searchBar.getText().split(" ");
            String items = String.join(" ", terms);
            Content[] c = web.search(items, 1, 1);
            if (displayContent(c, 1)) {
                pageHBox.setDisable(false);
            }
        } catch (Exception ex) {
            Logger.getLogger(FrontPageController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void searchTag(String tag) {
        try {
            Content[] c = web.search(tag, 1, 2);
            if (displayContent(c, 1)) {
                pageHBox.setDisable(false);
            }
        } catch (Exception ex) {
            Logger.getLogger(FrontPageController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean lastSearch() {
        try {
            Content[] c = web.lastPagedSearch(page);
            return displayContent(c, page);
        } catch (Exception ex) {
            Logger.getLogger(FrontPageController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public void updatePageText() {
        textPage.setText(String.valueOf(page));
        labelPage.setText("Current Page: " + page + " out of " + web.maxPage);

    }

    public boolean lastSearchPage(int page) {
        try {
            Content[] c = web.lastPagedSearch(page);
            return displayContent(c, page);
        } catch (Exception ex) {
            Logger.getLogger(FrontPageController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean displayContent(Content[] c, int page) {
        if (c != null) {
            this.content = c;
            testText.clear();
            ObservableList<String> imagefiles = FXCollections.observableArrayList();
            imagefiles.setAll(getCoverURLS(content));
            imageList.setItems(imagefiles);
            imageList.getSelectionModel().select(0);
            imageList.scrollTo(0);
            this.page = page;
            updatePageText();
            return true;
        } else {
            testText.setText("Your search returned no results");
            return false;
        }

    }

    public String[] getCoverURLS(Content[] content) {
        String[] images = new String[content.length];
        for (int i = 0; i < images.length; i++) {
            images[i] = "http:" + content[i].content_images.cover;
        }
        return images;

    }
    
    public void hidetagList() {
        if(tagListParent.getChildren().contains(tagList) == true) {
            tagListParent.getChildren().remove(tagList);
        } else {
            tagListParent.getChildren().add(tagList);
        }
        
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setUpImageList();
        setUpButtons();

        searchBar.setOnKeyPressed((event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                try {
                    searchTerm();
                } catch (Exception ex) {
                    Logger.getLogger(FrontPageController.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        textPage.setOnKeyPressed((event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                try {
                    int num = Integer.parseInt(textPage.getText());
                    lastSearchPage(num);
                } catch (Exception ex) {
                    Logger.getLogger(FrontPageController.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    public void setUpTagList() {
        System.out.println(web);
        ObservableList<String> tags = FXCollections.observableArrayList();
        tags.setAll(web.populateTags());
        tagList.setItems(tags);
        tagList.getSelectionModel().selectedItemProperty().addListener((obs, oldString, newString) -> {
            if (newString != null) {
                searchTag(newString);
            }

        });
    }

    public void setUpButtons() {
        try {
            btnNext.setOnAction((ActionEvent e) -> {
                page++;
                if (lastSearch() == false) {
                    page--;
                }
            });

            btnPrevious.setOnAction((ActionEvent e) -> {
                if (page >= 2) {
                    page--;
                    if (lastSearch() == false) {
                        page++;
                    }
                }
            });

        } catch (Exception ex) {
            Logger.getLogger(FrontPageController.class
                    .getName()).log(Level.SEVERE, null, ex);

        }

    }

    public void setUpImageList() {
        imageList.setCellFactory(x -> new ListCell<String>() {
            ImageView imageView = new ImageView();

            {
                imageView.setFitHeight(USE_PREF_SIZE);
                imageView.setFitWidth(USE_PREF_SIZE);
                imageView.setPreserveRatio(true);
            }

            @Override
            public void updateItem(String path, boolean empty) {
                super.updateItem(path, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(null);
                    imageView.setImage(new Image(path, true));
                    setGraphic(imageView);
                }
            }
        });

        imageList.getSelectionModel().selectedItemProperty().addListener((obs, oldImage, newImage) -> {

            if (newImage != null) {

                System.out.println(newImage);
                System.out.println(imageList.getSelectionModel().getSelectedIndex());
                int index = imageList.getSelectionModel().getSelectedIndex();
                Content c = content[index];
                String second = "http:" + c.content_images.sample;

                System.out.println(newImage);
                System.out.println(second);
                sampleImage.setImage(new Image(newImage, true));
                sampleImage.setFitHeight(sampleImage.getImage().getRequestedHeight());
                sampleImage.setFitWidth(sampleImage.getImage().getRequestedWidth());

                imageList.setMinWidth(sampleImage.getImage().getRequestedWidth());

                sampleImagetwo.setImage(new Image(second, true));
                sampleImagetwo.setFitHeight(sampleImagetwo.getImage().getRequestedHeight());
                sampleImagetwo.setFitWidth(sampleImagetwo.getImage().getRequestedWidth());

                tagbox.getChildren().clear();
                int length = c.content_tags.size();
                Button[] btnItems = new Button[length];
                for (int i = 0; i < length; i++) {
                    Button btnItem = new Button(c.content_tags.get(i).attribute);

                    btnItem.setPrefSize(100, 20);
                    btnItem.setOnAction((ActionEvent event) -> {
                        searchTag(btnItem.getText());
                    });
                    btnItems[i] = btnItem;

                }
                tagbox.getChildren().addAll(btnItems);
            }
        });

    }

}
