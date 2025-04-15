package lucaslimb.com.github.cryptomonitor.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import lucaslimb.com.github.cryptoMonitor.R
import lucaslimb.com.github.cryptomonitor.model.InfoItem

class InfoAdapter(private var items: MutableList<InfoItem>) :
    RecyclerView.Adapter<InfoAdapter.InfoViewHolder>() {

    class InfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val coin: TextView = itemView.findViewById(R.id.info_coin)
        val time: TextView = itemView.findViewById(R.id.info_time)
        val value: TextView = itemView.findViewById(R.id.info_value)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_info, parent, false)
        return InfoViewHolder(view)
    }

    override fun onBindViewHolder(holder: InfoViewHolder, position: Int) {
        val item = items[position]
        holder.coin.text = item.coin
        holder.time.text = item.time
        holder.value.text = item.value.toString()
    }

    override fun getItemCount(): Int = items.size

    fun addItemAtTop(item: InfoItem) {
        items.add(0, item)
        notifyItemInserted(0)
    }
}
