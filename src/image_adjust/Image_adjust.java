 



package image_adjust;

import java.io.File;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.util.Duration;


public class Image_adjust extends Application {
   
    private double scene_width=400;
    private double scene_height=450;
    private Image image;
    int binary=0;
    
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        
//                            SETUP IMAGE AND IMAGE VIEW
        image=new Image("file:");
        ImageView image_view=new ImageView(image);
        image_view.setFitWidth(200);                                                        //empty file with width as place holder
        image_view.setFitHeight(200);                                                       //empty file with height as place holder
//                            SETUP MENU BAR
        MenuBar menu_bar=new MenuBar();
        Menu file_menu=new Menu("File");
        menu_bar.getMenus().add(file_menu);
//                            SETUP MENU ITEMS
        MenuItem open_item=new MenuItem("Open");
        file_menu.getItems().add(open_item);
        
        MenuItem exit_item=new MenuItem("Exit");
        file_menu.getItems().add(exit_item);
//                            SETUP SLIDER
        Slider slider=new Slider(0,100,100);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
//                            SETUP LABEL
        Label percent_value=new Label("100%");
        percent_value.setId("percent_value");
//                            SETUP BUTTON
        Button glow_btn=new Button("Glow Effect");
        
//                            SLIDER LISTENER EVENT
        slider.valueProperty().addListener(
            (observeable, oldvalue, newvalue) ->{
                double slider_value=slider.getValue();                                          //get slider value
                
                FadeTransition ftrans=new FadeTransition(new Duration(2), image_view);
                double fade_slider_value=slider_value/100;                                      //set slider value by dividing by 100 for transition set to value
                
                ftrans.setToValue(fade_slider_value);
                ftrans.play();
                percent_value.setText(String.format("%.2f", slider_value)+"%");                 //display slider value
        });

//                            OPEN FILE EVENT
        open_item.setOnAction(event ->{
           FileChooser file_chooser=new FileChooser(); 
           File selected_file=file_chooser.showOpenDialog(primaryStage);                        //setup file chooser to show
           
           if (selected_file != null){
               String file_name=selected_file.getPath();
               
               image=new Image("file:"+file_name);                                              //replace image directory with new one from file chooser
               double selected_image_height=image.getHeight();                                  //get new image height
               double selected_image_width=image.getWidth();                                    //get new image width
               
               if (selected_image_height>700){                                                  //if image height over 700, resize image to half of height and width
                   selected_image_height=selected_image_height/2;
                   selected_image_width=selected_image_width/2;
               }
               
               primaryStage.setWidth(selected_image_width+200);                                 //make stage width change if image size changes
               primaryStage.setHeight(selected_image_height+300);                               //make stage width change if image size changes
               image_view.setFitWidth(selected_image_width);                                    //set image view width to new image size
               image_view.setFitHeight(selected_image_height);                                  //set image view height to new image size
               image_view.setImage(image);                                                      //replace old image with newly selected one
                                    
               Rectangle2D primaryBounds=Screen.getPrimary().getVisualBounds();                 //get primary screen size
               primaryStage.setX((primaryBounds.getWidth()-primaryStage.getWidth())/2);         //re-center Stage on screen width
               primaryStage.setY((primaryBounds.getHeight()-primaryStage.getHeight())/2);       //re-center Stage on screen height
           }
        });
        
//                            EXIT FILE EVENT
        exit_item.setOnAction(event ->{
           primaryStage.close(); 
        });
        
//                            SETUP GLOW EFFECT
        Glow glow=new Glow();
        image_view.setEffect(glow);
        glow.setLevel(0);
//                            SETUP DROPSHADOW FOR BUTTON
        DropShadow drop_shadow=new DropShadow();
        drop_shadow.setOffsetX(4);
        drop_shadow.setOffsetY(4);
        drop_shadow.setColor(Color.WHITE);
//                            SETUP INNER SHADOW FOR BUTTON
        InnerShadow inner_shadow=new InnerShadow();
        inner_shadow.setOffsetX(8);
        inner_shadow.setOffsetY(8);
        
        glow_btn.setEffect(drop_shadow);                             //set drop shadow on for button on startup of program
        
//                            GLOW BUTTON LAMBDA EVENT
        glow_btn.setOnAction(event ->{
            if (binary==0){                                         //if binary=0, set glow level to .8 and inner shadow for button
                binary=binary+1;
                glow.setLevel(.8);
                glow_btn.setEffect(inner_shadow);
            }
            else{                                                   //if binary=1, set flow level to 0 and drop shadow for buttom
                binary=binary-1;
                glow.setLevel(0);
                glow_btn.setEffect(drop_shadow);
            }
        });
        
        BorderPane border_pane=new BorderPane();
        border_pane.setTop(menu_bar);
        HBox hbox=new HBox(image_view);
        hbox.setAlignment(Pos.CENTER);
        hbox.setPadding(new Insets(20,0,0,0));
        VBox vbox=new VBox(border_pane, hbox, slider, percent_value, glow_btn);
        vbox.setAlignment(Pos.TOP_CENTER);

        Scene scene = new Scene(vbox, scene_width, scene_height);
        scene.getStylesheets().add("styles.css");
        primaryStage.setTitle("Image Effects");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
