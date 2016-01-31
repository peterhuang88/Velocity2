package org.example.lockscreen;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;


public class MainActivity extends Activity {

    //peter
    final Context context = this;
    private Button button;
    private TextView result;

    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;

    String name = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set up our Lockscreen
        makeFullScreen();
        startService(new Intent(this, LockScreenService.class));


        setContentView(R.layout.activity_main);


        //peter
        button = (Button) findViewById(R.id.buttonPrompt);
        result = (TextView) findViewById(R.id.editTextResult);


        final File f = new File("name.txt");


        if ( !f.exists() || f.length() == 0 ) {

        //peter add button listener

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {

                    // get prompts.xml view
                    //if (result == null) {
                    LayoutInflater li = LayoutInflater.from(context);
                    View promptsView = li.inflate(R.layout.prompts, null);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                    // set prompts.xml to alertdialog builder
                    alertDialogBuilder.setView(promptsView);

                    final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);

                    // set dialog message
                    alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // get user input and set it to result
                            // edit text

                            readName();


                            result.setText(userInput.getText());
                            name = result.getText().toString();





                        }
                    })
                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            onStop();
                                            //dialog.cancel();
                                        }
                                    });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();

                }
            });
        }



        lvItems = (ListView) findViewById(R.id.lvItems);
        items = new ArrayList<String>();

        //PERSIST
        readItems();
        itemsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        //items.add("Finish HW");
        //items.add("Go lift and get swole");

        setupListViewListener();







        // change the text of a textView dynamically
        TextView text = (TextView) findViewById (R.id.textView);
        TextView goodMorningEvening = (TextView) findViewById (R.id.good);

        // ALICE"S QUOTES ARE HERE
        text.setText(getQuote());


        Calendar c = Calendar.getInstance();
        int hours = c.get(Calendar.HOUR_OF_DAY);

        if (hours > 12)
            goodMorningEvening.setText("Good Evening");
        else
            goodMorningEvening.setText("Good Morning");


        // BROKEN :-(
        //TextView editTextResult = (TextView) findViewById(R.id.editTextResult);
        //editTextResult.setText(name);

        RelativeLayout mainlay = (RelativeLayout)findViewById(R.id.back);
        mainlay.setBackgroundResource(getDrawableID());

        // END OF ONCREATE HEUHEUHHE
    }


    private void readItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            items = new ArrayList<String>();
        }
    }

    private void writeItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void readName() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "name.txt");
        try {
            name = FileUtils.readFileToString(todoFile);
        } catch (IOException e) {
            name = "fuk u scrub";
        }
    }

    private void writeName() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "name.txt");
        try {
            FileUtils.writeStringToFile(todoFile, name);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        itemsAdapter.add(itemText);
        etNewItem.setText("");
        writeItems();
    }




    public int getDrawableID()
    {
        Random rand = new Random();

        int[] drawableArray = {R.drawable.bamboo, R.drawable.bristlegrass, R.drawable.deathvalley, R.drawable.eaglewaterfall,  R.drawable.elephant, R.drawable.fall, R.drawable.lake,  R.drawable.red,  R.drawable.zebras,  R.drawable.transistor, R.drawable.cloudy, R.drawable.rain, R.drawable.goldenpalace, R.drawable.red, R.drawable.silhouette};
        return drawableArray[rand.nextInt(drawableArray.length)];
    }


    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter,
                                                   View item, int pos, long id) {
                        // Remove the item within array at position
                        items.remove(pos);
                        // Refresh the adapter
                        itemsAdapter.notifyDataSetChanged();
                        // Return true consumes the long click event (marks it handled)
                        writeItems();
                        return true;
                    }

                });
    }

    public String getQuote()
    {
        String[] quotes = {"Art should comfort the disturbed and disturb the comfortable. - Cesar A. Cruz",
                "If you are silent about your pain, they will kill you and say you enjoyed it. - Zora Neale Hurston",
                "In the depth of winter, I finally learned that there was within me an invincible summer. - Albert Camus",
                "You must do the things you think you cannot do. - Eleanor Roosevelt",
                "We must let go of the life we have planned, so as to accept the one that is waiting for us. - Joseph Campbell",
                "Keep your face always toward the sunshine - and shadows will fall behind you. - Walt Whitman",
                "It is always the simple that produces the marvelous. - Amelia Barr",
                "From a small seed a mighty trunk may grow. - Aeschylus",
                "What we achieve inwardly will change outer reality. - Plutarch",
                "Man must live and create. Live to the point of tears. - Albert Camus",
                "The harder the conflict, the more glorious the triumph. - Thomas Paine",
                "I wish that every human life might be pure transparent freedom. - Simone de Beauvoir",
                "Like all dreamers, I mistook disenchantment for truth. - Jean-Paul Sartre",
                "Tomorrow is always fresh, with no mistakes in it yet. - L. M. Montgomery, Anne of Green Gables",
                "And now that you don’t have to be perfect, you can be good. - John Steinbeck, East of Eden"
        };

        int quoteNum;
        Random rand = new Random();
        quoteNum = rand.nextInt(16);

        return quotes[quoteNum];
    }


    /**
     * A simple method that sets the screen to fullscreen.  It removes the Notifications bar,
     *   the Actionbar and the virtual keys (if they are on the phone)
     */
    public void makeFullScreen() {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if(Build.VERSION.SDK_INT < 19) { //View.SYSTEM_UI_FLAG_IMMERSIVE is only on API 19+
            this.getWindow().getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        } else {
            this.getWindow().getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE);
        }
    }

    @Override
    public void onBackPressed() {
        return; //Do nothing!
    }

    public void unlockScreen(View view) {
        //Instead of using finish(), this totally destroys the process
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
