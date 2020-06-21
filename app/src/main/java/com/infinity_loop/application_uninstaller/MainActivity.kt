package com.infinity_loop.application_uninstaller

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
//import android.support.v7.app.AlertDialog
//import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity

import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Build
import android.provider.Settings
import android.widget.*
import androidx.annotation.RequiresApi
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import java.util.*
import kotlin.collections.ArrayList
import java.util.Arrays


class MainActivity : AppCompatActivity() {

    private var installedApps: List<AppList>? = null
    private var installedAppAdapter: AppAdapter? = null
    internal lateinit var userInstalledApps: ListView
    lateinit var mAdView : AdView
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button = findViewById<Button>(R.id.btn)
        button.setOnClickListener {
            val intent = Intent(this, string::class.java)
            startActivity(intent)
        }

        MobileAds.initialize(this,getString(R.string.admob_app_id))
            mAdView = findViewById(R.id.adView)
            val adRequest = AdRequest.Builder().build()
            mAdView.loadAd(adRequest)



  
        userInstalledApps = findViewById(R.id.installed_app_list) as ListView

        installedApps = getInstalledApps()
        installedAppAdapter = AppAdapter(this@MainActivity, installedApps!!)
        userInstalledApps.adapter = installedAppAdapter
        userInstalledApps.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, i, l ->
                val colors = arrayOf(" Uninstall", " Info")
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Choose Action")
                    .setItems(colors, DialogInterface.OnClickListener { dialog, which ->
                        // The 'which' argument contains the index position of the selected item
                        if (which == 0) {
                            val intent = Intent(Intent.ACTION_UNINSTALL_PACKAGE)
                            intent.data = Uri.parse("package:" + installedApps!![i].packages)
                            if (intent != null) {
                                startActivity(intent)
                            } else {
                                Toast.makeText(
                                    this@MainActivity,
                                    installedApps!![i].packages + " Error, Please Try Again...",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        if (which == 1) {
                            val intent =
                                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            intent.data = Uri.parse("package:" + installedApps!![i].packages)
                            Toast.makeText(
                                this@MainActivity,
                                installedApps!![i].packages,
                                Toast.LENGTH_SHORT
                            ).show()
                            startActivity(intent)
                        }
                    })
                builder.show()
            }

        //Total Number of Installed-Apps(i.e. List Size)
        val abc = userInstalledApps.count.toString() + ""
        val countApps = findViewById(R.id.countApps) as TextView
        countApps.text = "Total Installed Apps: $abc"
        Toast.makeText(this, "$abc Apps", Toast.LENGTH_SHORT).show()

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getInstalledApps(): List<AppList> {
        val pm = getPackageManager()
        var apps = ArrayList<AppList>()
        var installed = ArrayList<AppList>()
//        var custom_package: ArrayList<String> = ArrayList(20)
//        custom_package.add("com.flipkart.android")
//        val strings = arrayOf("com.flipkart.android")
        var strings = string().array

        var i = 0
        val packs = getPackageManager().getInstalledPackages(0)
//        List<PackageInfo> packs = getPackageManager().getInstalledPackages(PackageManager.GET_PERMISSIONS);
        for (i in packs.indices) {
            val p = packs.get(i)
            if (!isSystemPackage(p)) {
                val appName = p.applicationInfo.loadLabel(getPackageManager()).toString()
                val icon = p.applicationInfo.loadIcon(getPackageManager())
                val packages = p.applicationInfo.packageName
//              installed.add(AppList(appName, icon, packages))
//                var compared = packages.toSet().intersect(custom_package.toSet()).toSet()
                val found = Arrays.stream(strings).anyMatch { t -> t == packages }
                if (found)
                {
                    apps.add(AppList(appName, icon, packages))
                }
            }
        }
        return apps
    }


    private fun isSystemPackage(pkgInfo: PackageInfo): Boolean {
        return pkgInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
    }

    inner class AppAdapter(context: Context, var listStorage: List<AppList>) : BaseAdapter() {

        var layoutInflater: LayoutInflater

        init {
            layoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }

        override fun getCount(): Int {
            return listStorage.size
        }

        override fun getItem(position: Int): Any {
            return position
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var convertView = convertView


            val listViewHolder: ViewHolder

            if (convertView == null) {
                listViewHolder = ViewHolder()
                convertView = layoutInflater.inflate(R.layout.app_view, parent, false)

                listViewHolder.textInListView =
                    convertView!!.findViewById<View>(R.id.list_app_name) as TextView
                listViewHolder.imageInListView =
                    convertView.findViewById<View>(R.id.app_icon) as ImageView
                listViewHolder.packageInListView =
                    convertView.findViewById<View>(R.id.app_package) as TextView
                convertView.tag = listViewHolder
            } else {
                listViewHolder = convertView.tag as ViewHolder
            }
            listViewHolder.textInListView!!.text = listStorage[position].name
            listViewHolder.imageInListView!!.setImageDrawable(listStorage[position].icon)
            listViewHolder.packageInListView!!.text = listStorage[position].packages

            return convertView
        }

        internal inner class ViewHolder {
            var textInListView: TextView? = null
            var imageInListView: ImageView? = null
            var packageInListView: TextView? = null
        }
    }

    inner class AppList(val name: String, icon: Drawable, val packages: String) {
        var icon: Drawable
            internal set

        init {
            this.icon = icon
        }


    }



}
