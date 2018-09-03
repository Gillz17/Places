package gillz.android.com.places;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    //Adds the buttons to be able to sign in using these methods
    List providers = Arrays.asList(
            new AuthUI.IdpConfig.EmailBuilder().build(),
            new AuthUI.IdpConfig.GoogleBuilder().build());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        mDrawerLayout = findViewById(R.id.drawer_layout);

        mNavigationView = findViewById(R.id.nav_view);

        mNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        //Set item as selected to persist highlight
                        menuItem.setChecked(true);

                        //Close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        int id = menuItem.getItemId();
                        //TODO: Update UI
                        if (id == R.id.nav_people) {
                            Toast.makeText(MainActivity.this, "People", Toast.LENGTH_SHORT).show();
                        } else if (id == R.id.nav_groups){
                            Toast.makeText(MainActivity.this, "Groups", Toast.LENGTH_SHORT).show();
                        }else if (id == R.id.nav_invites){
                            Toast.makeText(MainActivity.this, "Invites", Toast.LENGTH_SHORT).show();
                        }else if (id == R.id.nav_rest){
                            Toast.makeText(MainActivity.this, "Rest", Toast.LENGTH_SHORT).show();
                        }else if (id == R.id.nav_cafe){
                            Toast.makeText(MainActivity.this, "Cafe", Toast.LENGTH_SHORT).show();
                        }else if (id == R.id.nav_shopping){
                            Toast.makeText(MainActivity.this, "Shopping", Toast.LENGTH_SHORT).show();
                        }else if (id == R.id.nav_groc){
                            Toast.makeText(MainActivity.this, "Groc", Toast.LENGTH_SHORT).show();
                        }else if (id == R.id.nav_movies){
                            Toast.makeText(MainActivity.this, "Movies", Toast.LENGTH_SHORT).show();
                        }else if (id == R.id.nav_events){
                            Toast.makeText(MainActivity.this, "Events", Toast.LENGTH_SHORT).show();
                        }else if (id == R.id.sign_out){
                            signOut();
                        }else if(id == R.id.delete){
                            delete();
                        }
                        return true;
                    }
                }
        );

        if (user != null){
            //User is signed in
            getUserDetails();

        }else{//User is not signed in
            //Create and launch the sign in page
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build(),
                    RC_SIGN_IN);
            getUserDetails();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN){
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK){
                //Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            }//TODO: The else statements for signing in
        }
    }

    public void signOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        //User is now signed out
                        startActivityForResult(
                                AuthUI.getInstance()
                                        .createSignInIntentBuilder()
                                        .setAvailableProviders(providers)
                                        .build(),
                                RC_SIGN_IN);
                    }
                });
    }
    public void delete() {
        //Display an alert informing user of action
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Delete User")
                .setMessage("Are you sure you want to delete your account?  This action cannot be undone.")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Continue Delete
                        AuthUI.getInstance()
                                .delete(MainActivity.this)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        //Take the user back to the sign in page
                                        startActivityForResult(
                                                AuthUI.getInstance()
                                                        .createSignInIntentBuilder()
                                                        .setAvailableProviders(providers)
                                                        .build(),
                                                RC_SIGN_IN);
                                    }
                                });
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void getUserDetails(){
        View hView = mNavigationView.getHeaderView(0);

        TextView nav_user = (TextView)hView.findViewById(R.id.nav_user);
        nav_user.setText(user.getDisplayName());

        TextView nav_email = (TextView)hView.findViewById(R.id.nav_email);
        nav_email.setText(user.getEmail());

        Uri photo = user.getPhotoUrl();
        ImageView nav_image = (ImageView)hView.findViewById(R.id.nav_user_icon);
        nav_image.setImageURI(photo);
    }
}
