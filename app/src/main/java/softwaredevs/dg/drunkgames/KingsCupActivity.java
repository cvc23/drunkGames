package softwaredevs.dg.drunkgames;

import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class KingsCupActivity extends AppCompatActivity implements View.OnTouchListener, View.OnDragListener, KingsCupData{

    private ImageView card;
    private TextView textView;
    private int xpos=-1, ypos=-1;
    private int [] loc = new int [2];
    public boolean count = false;
    ArrayList <Integer> cards;
    private int kingsCount=0;
    ArrayList <Integer> challenge;
    Rect rectf = new Rect();
    private boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kings_cup);
        card = (ImageView)findViewById(R.id.imageViewKingsCupCard);
        textView = (TextView) findViewById(R.id.textViewKingsCup);
        card.setOnTouchListener(this);
        findViewById(R.id.kingsCupView).setOnDragListener(this);
        cards = new ArrayList<>();
        challenge = new ArrayList<>();
        for (int x= 0; x<cardsData.length; x++) {
            cards.add(cardsData[x]);
            challenge.add(challengeData[x]);
        }

    }

    private void changeCard(){
        Random rd = new Random();
        int rand = rd.nextInt(cards.size()-1);
        card.setImageResource(cards.get(rand));
        cards.remove(rand);
        textView.setText(getString(challenge.get(rand)));
        challenge.remove(rand);
        if(challenge.get(rand)== R.string.copareyk){
            kingsCount++;
        }
        if(kingsCount==4){
            endGame();
        }

    }

    private void endGame(){
        //show an alert to end the game and finish the activity
        CustomDialog dialog = new CustomDialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        //finish();
    }

    @Override
    public void onWindowFocusChanged (boolean hasFocus) {
        final int[] x=new int[2];
        card.getLocationOnScreen(x);
    }

    @Override
    public boolean onTouch(View v, MotionEvent e) {

        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                v.startDrag(null, shadowBuilder, v, 0);
                return true;
        }

        return false;

    }

    @Override
    public boolean onDrag(View mainView, DragEvent e) {
        if(!flag){
            Rect offsetViewBounds = new Rect();
            //returns the visible bounds
            card.getDrawingRect(offsetViewBounds);
            // calculates the relative coordinates to the parent
            ViewGroup  parentViewGroup = findViewById(R.id.kingsCupView);
            parentViewGroup.offsetDescendantRectToMyCoords(card, offsetViewBounds);

            xpos = offsetViewBounds.left;
            ypos = offsetViewBounds.top;
            flag=true;

        }
        View view = (View) e.getLocalState();
        switch (e.getAction()) {
            case DragEvent.ACTION_DROP:
                view.setX(e.getX() - (view.getWidth() / 2));
                view.setY(e.getY() - (view.getHeight() / 2));
                if(view.getX()!= xpos || view.getY() != ypos){
                    //eliminar carta
                    //Toast.makeText(mainView.getContext(), "Cambio de carta", Toast.LENGTH_SHORT).show();
                    view.setX(xpos);
                    view.setY(ypos);
                    changeCard();
                }
                view.invalidate();
                mainView.invalidate();
                return true;
            case DragEvent.ACTION_DRAG_STARTED:
                return true;

            case DragEvent.ACTION_DRAG_EXITED:
                break;

            case DragEvent.ACTION_DRAG_ENDED:
                mainView.invalidate();
                return true;

            default:


                break;
        }

        return true;
    }



    }
