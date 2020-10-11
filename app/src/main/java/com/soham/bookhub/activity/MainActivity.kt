package com.soham.bookhub.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.soham.bookhub.R
import com.soham.bookhub.fragment.AboutFragment
import com.soham.bookhub.fragment.DashboardFragment
import com.soham.bookhub.fragment.ProfileFragment
import com.soham.bookhub.fragment.favouritesFragment

class MainActivity : AppCompatActivity() {
    lateinit var drawerLayout:DrawerLayout
    lateinit var coordinatorLayout:CoordinatorLayout
    lateinit var toolbar: Toolbar
    lateinit var navigationView:NavigationView
    lateinit var frameLayout: FrameLayout
    var  previousMenuItem:MenuItem?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawerLayout = findViewById(R.id.drawer)
        coordinatorLayout = findViewById(R.id.coordinator)
        toolbar = findViewById(R.id.toolbar)
        navigationView = findViewById(R.id.navigation)
        frameLayout = findViewById(R.id.frameLayout)
        setToolbar()
        opendashboard()
        //to convert home toggle into hamburger
        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this@MainActivity,
            drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )
        //adds listener to the toggle button in drawer layout
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState() //sync the actionbar toggle (hamburger) with the drawer
         navigationView.setNavigationItemSelectedListener {

             if(previousMenuItem!=null){
                 previousMenuItem?.isChecked=false
             }
             it.isCheckable=true
             it.isChecked=true
             previousMenuItem=it

             when(it.itemId)
             {
                 R.id.dashboard -> {
                    opendashboard()
                 }
                 R.id.favourites ->{
                     supportFragmentManager.beginTransaction()
                         .replace(
                             R.id.frameLayout,
                             favouritesFragment()
                         )
                         .commit()
                     drawerLayout.closeDrawers()
                     supportActionBar?.title="Favourites"
                 }
                 R.id.profile ->{
                     supportFragmentManager.beginTransaction()
                         .replace(
                             R.id.frameLayout,
                             ProfileFragment()
                         )
                         .commit()
                     drawerLayout.closeDrawers()
                     supportActionBar?.title="Profile"
                 }
                 R.id.about ->{
                     supportFragmentManager.beginTransaction()
                         .replace(
                             R.id.frameLayout,
                             AboutFragment()
                         )
                         .commit()
                     drawerLayout.closeDrawers()
                     supportActionBar?.title="About App"
                 }

             }
             return@setNavigationItemSelectedListener true
         }
    }

    //making  toolbar to be treated as action bar
    fun setToolbar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title="BookHub"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    //to launch the navigate drawer after getting clicked
    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        val id=item.itemId
        if(id==android.R.id.home)
        {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }
    fun opendashboard(){
        val fragment= DashboardFragment()
        val transaction=supportFragmentManager.beginTransaction()
        transaction.replace(
            R.id.frameLayout,
            DashboardFragment()
        )
        transaction.commit()
        supportActionBar?.title="Dashboard"
        navigationView.setCheckedItem(R.id.dashboard)
    }
    override fun onBackPressed(){
        val frag= supportFragmentManager.findFragmentById(R.id.frameLayout)
        when(frag){
            !is DashboardFragment -> opendashboard()
            else -> super.onBackPressed()
        }
    }
}

