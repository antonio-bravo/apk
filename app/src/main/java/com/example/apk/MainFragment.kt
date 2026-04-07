package com.example.apk

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.*
import androidx.lifecycle.ViewModelProvider

class MainFragment : BrowseSupportFragment(), OnItemViewClickedListener {

    private lateinit var viewModel: ChannelViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title = getString(R.string.browse_title)

        viewModel = ViewModelProvider(this)[ChannelViewModel::class.java]

        viewModel.channels.observe(this) { channels ->
            loadRows(channels)
        }

        viewModel.loadChannels()

        onItemViewClickedListener = this
    }

    private fun loadRows(channels: List<Channel>) {
        val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())
        val cardPresenter = CardPresenter()

        val listRowAdapter = ArrayObjectAdapter(cardPresenter)
        for (channel in channels) {
            listRowAdapter.add(channel)
        }
        val header = HeaderItem(0, "Canales")
        rowsAdapter.add(ListRow(header, listRowAdapter))

        adapter = rowsAdapter
    }

    override fun onItemClicked(
        itemViewHolder: Presenter.ViewHolder?,
        item: Any?,
        rowViewHolder: RowPresenter.ViewHolder?,
        row: Row?
    ) {
        if (item is Channel) {
            try {
                // Configurar el Intent para abrir específicamente con Acestream
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(Uri.parse(item.url), "application/x-mpegurl")
                    // Intentamos forzar que lo abra el motor de Acestream si está instalado
                    `package` = "org.acestream.media" 
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                
                // Si falla con el paquete específico, probamos el genérico para que el sistema pregunte
                if (intent.resolveActivity(requireContext().packageManager) != null) {
                    startActivity(intent)
                } else {
                    val genericIntent = Intent(Intent.ACTION_VIEW, Uri.parse(item.url))
                    startActivity(genericIntent)
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error al abrir Acestream: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}