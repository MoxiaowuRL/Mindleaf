import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mindleaf.R
import com.example.mindleaf.data.Quote

class FavoriteQuotesAdapter(
    private var favoriteQuotes: MutableList<Quote>,
    private val onRemoveClick: (Quote) -> Unit
) : RecyclerView.Adapter<FavoriteQuotesAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val quoteContentTextView: TextView = itemView.findViewById(R.id.favoriteQuoteContent)
        val quoteAuthorTextView: TextView = itemView.findViewById(R.id.favoriteQuoteAuthor)
        val removeButton: ImageButton = itemView.findViewById(R.id.removeFavoriteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_favorite_quote, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val favoriteQuote = favoriteQuotes[position]
        holder.quoteContentTextView.text = favoriteQuote.content
        holder.quoteAuthorTextView.text = favoriteQuote.author
        holder.removeButton.setOnClickListener {
            onRemoveClick(favoriteQuote)
        }
    }
    fun removeFavoriteQuote(quote: Quote) {
        val position = favoriteQuotes.indexOf(quote)
        if (position != -1) {
            favoriteQuotes.removeAt(position)
            notifyItemRemoved(position)
        }
    }
    override fun getItemCount(): Int {
        return favoriteQuotes.size
    }

    fun updateFavoriteQuotes(newFavoriteQuotes: List<Quote>) {
        favoriteQuotes.clear()
        favoriteQuotes.addAll(newFavoriteQuotes)
        notifyDataSetChanged()
    }

}