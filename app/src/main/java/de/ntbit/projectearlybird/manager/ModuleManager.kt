package de.ntbit.projectearlybird.manager

import android.content.Intent
import android.os.Handler
import android.os.Looper
import com.parse.ParseQuery
import com.parse.livequery.ParseLiveQueryClient
import com.parse.livequery.SubscriptionHandling
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import de.ntbit.projectearlybird.R
import de.ntbit.projectearlybird.helper.ApplicationContextProvider
import de.ntbit.projectearlybird.helper.NotificationHelper
import de.ntbit.projectearlybird.model.Group
import de.ntbit.projectearlybird.model.Module
import de.ntbit.projectearlybird.ui.activity.GroupActivity
import java.net.URI

/**
 * Provides an object of [ModuleManager] which is used for interacting
 * with objects of the [Module] model.
 * @property simpleClassName
 * @property moduleList holds a list of modules found in parse database
 */

class ModuleManager {
    private val simpleClassName = this.javaClass.simpleName

    private val parseLiveQueryClient: ParseLiveQueryClient =
        ParseLiveQueryClient.Factory.getClient(URI("wss://projectearlybird.back4app.io/"))
    private lateinit var adapter: GroupAdapter<GroupieViewHolder>
    private val moduleList = ArrayList<Module>()

    init {
        getModulesFromParse()
        //listenForNewModules()
    }

    private fun getModulesFromParse() {
        val query = ParseQuery.getQuery(Module::class.java)
        query.findInBackground { modules, _ ->
            moduleList.addAll(modules)
        }
    }

    fun getModules() : Collection<Module>{ return moduleList }

    fun listenForNewModules() {
        val parseQuery = ParseQuery.getQuery(Module::class.java)
        val subscriptionHandling: SubscriptionHandling<Module> = parseLiveQueryClient.subscribe(parseQuery)

        subscriptionHandling.handleEvent(SubscriptionHandling.Event.CREATE) { _, module ->
            val handler = Handler(Looper.getMainLooper())
            handler.post {
                processNewModule(module)
            }
        }
    }

    private fun processNewModule(module: Module) {
        moduleList.add(module)
    }

    fun getAdapter(): GroupAdapter<GroupieViewHolder> { return adapter }
}