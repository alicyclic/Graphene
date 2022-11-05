package com.github.lua

import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import org.luaj.vm2.LuaFunction
import org.luaj.vm2.lib.jse.CoerceJavaToLua


class EventRegistry(private val javaPlugin: JavaPlugin) : Listener {
    private val events: HashMap<String, ArrayList<LuaFunction>> by lazy(::HashMap)

    init {
        "BlockBreakEvent".register()
    }

    fun registerEvents() = javaPlugin.server.pluginManager.registerEvents(this, javaPlugin)

    fun isEvent(string: String): Boolean = events.contains(string)

    fun get(string: String): ArrayList<LuaFunction>? = events[string]

    private fun String.register() {
        events.putIfAbsent(this, ArrayList())
    }

    @EventHandler
    fun onEvent(e: Event) {
        val callbacks: ArrayList<LuaFunction> = events[e.eventName] ?: return
        callbacks.forEach {
            it.call(CoerceJavaToLua.coerce(e as Any))
        }
    }

}