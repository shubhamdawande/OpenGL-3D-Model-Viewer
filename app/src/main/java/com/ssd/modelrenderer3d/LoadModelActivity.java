package com.ssd.modelrenderer3d;

import min3d.core.Object3dContainer;
import min3d.core.RendererActivity;
import min3d.parser.IParser;
import min3d.parser.Parser;
import min3d.vos.Light;

public class LoadModelActivity extends RendererActivity {

    private Object3dContainer Object3D;
    public static String STRING_EXTRA;

    /** Called when the activity is first created. */
    @Override
    public void initScene() {
        String fileName = null;
        if (getIntent().hasExtra(STRING_EXTRA)) {
            fileName = getIntent().getStringExtra(STRING_EXTRA);
        }

        scene.lights().add(new Light());
        scene.lights().add(new Light());
        Light myLight = new Light();
        myLight.position.setZ(150);
        scene.lights().add(myLight);

        IParser myParser = Parser.createParser(Parser.Type.OBJ, getResources(), "com.ssd.modelrenderer3d:raw/"+fileName+"_obj",true);
        myParser.parse();
        Object3D = myParser.getParsedObject();
        Object3D.position().x = Object3D.position().y = Object3D.position().z = 0;

        // Depending on the model you will need to change the scale
        float scale = 1;
        switch(fileName){
            case "cube":
                scale = 0.5f;
                break;
            case "Camaro":
                scale = 0.25f;
                break;
            case "ship":
                scale = 2.0f;
                break;
            case "giftbox":
                scale = 0.05f;
                break;
            case "house":
                scale = 0.005f;
                break;
        }
        Object3D.scale().x = Object3D.scale().y = Object3D.scale().z = scale;
        scene.addChild(Object3D);
    }
}