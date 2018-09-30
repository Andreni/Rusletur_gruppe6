package no.hiof.informatikk.gruppe6.rusletur;

import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class ActionBar extends AppCompatActivity{

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.actionbar_menu, menu);
            getSupportActionBar().setTitle("RusleTur");
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {

            // TODO 1.0: Add button action for actionbar

            switch(item.getItemId()) {
                //TODO 1.1: Add action for home button
                case R.id.action_home:
                    //Intent intent = new Intent(this, home.class);
                    //startActivity(intent);
                    //return true;
                    writeMessageToUser("Home clicked");

                    //TODO 1.2: Add action for settings button
                case R.id.action_settings:
                    //Intent intent = new Intent(this, settings.class);
                    //startActivity(intent);
                    //return true;
                    writeMessageToUser("Settings clicked");
                default:
                    return super.onOptionsItemSelected(item);
            }
        }

        //Send message to user:
        private void writeMessageToUser(String messageToUser){
            Toast.makeText(this,messageToUser.toString(),Toast.LENGTH_SHORT).show();
        }
    }