package com.ssd.modelrenderer3d;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;

public class MenuActivity extends ListActivity {

    private enum Action { How_To, Load_3D_Shape, Load_3D_Model, Exit, Unknown };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        setListAdapter(new ArrayAdapter<>(this, R.layout.menu_item, getResources().getStringArray(R.array.menu_items)));
    }

    // Check when user selects 'load model'
    @Override
    public void onListItemClick(ListView l, View v, int position, long id){
        String selectedItem = (String) getListView().getItemAtPosition(position);
        String selectedAction = selectedItem.replace(' ', '_');
        Action action = Action.Unknown;
        action = Action.valueOf(selectedAction);
        switch(action){
            case How_To:
                showInfo();
                break;
            case Load_3D_Shape:
                loadShape();
                break;
            case Load_3D_Model:
                loadModel();
                break;
            case Exit:
                MenuActivity.this.finish();
                break;
        }
    }

    // show helpful info
    private void showInfo(){
        MenuActivity.this.startActivity(new Intent(this, InfoActivity.class));
    }

    // Load an existing OBJ Model from resources
    private void loadModel(){

        final String[] modelItems = getResources().getStringArray(R.array.model_items);
        createChooserDialog("Choose a model", modelItems, (String itemName)->{
            if(itemName != null){
                Intent intent = new Intent(this, LoadModelActivity.class);
                intent.putExtra(LoadModelActivity.STRING_EXTRA, itemName.toLowerCase());
                MenuActivity.this.startActivity(intent);
            }
        });
    }

    @FunctionalInterface
    public interface Callback {
        void onClick(String asset);
    }

    // create chooser dialog
    private void createChooserDialog(String Title, String[] itemArray, Callback callback){

        AlertDialog.Builder dialogBox = new AlertDialog.Builder(this);
        dialogBox.setTitle(Title);
        dialogBox.setMessage(null);
        dialogBox.setNegativeButton("Cancel", (dialog, id) -> callback.onClick(null));

        dialogBox.setItems(itemArray, (dialog, which) -> {
            String selectedItem = itemArray[which];
            callback.onClick(selectedItem);
        });
        dialogBox.create().show();
    }

    // Render a primitive shape using OpenGL -> PrimitiveShape, Pyramid
    private void loadShape() {

        final String[] items = getResources().getStringArray(R.array.primitive_items);
        createChooserDialog("Choose a shape", items, (String itemName)->{
            if(itemName != null){
                Intent intent = new Intent(this, LoadShapeActivity.class);
                intent.putExtra(LoadShapeActivity.STRING_EXTRA, itemName.toLowerCase());
                MenuActivity.this.startActivity(intent);
            }
        });
    }

}
