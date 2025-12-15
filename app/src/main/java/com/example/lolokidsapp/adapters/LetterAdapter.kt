package com.example.lolokidsapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.lolokidsapp.R
import com.example.lolokidsapp.models.Letter
import com.example.lolokidsapp.models.LetterType

/**
 * Adapter for displaying letters in a grid
 */
class LetterAdapter(
    private val letters: List<Letter>,
    private val onLetterClick: (Letter) -> Unit
) : RecyclerView.Adapter<LetterAdapter.LetterViewHolder>() {

    inner class LetterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val letterText: TextView = itemView.findViewById(R.id.letterText)
        val cardView: CardView = itemView as CardView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LetterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_letter, parent, false)
        return LetterViewHolder(view)
    }

    override fun onBindViewHolder(holder: LetterViewHolder, position: Int) {
        val letter = letters[position]
        holder.letterText.text = letter.character

        // Set card color based on letter type
        val backgroundColor = when (letter.type) {
            LetterType.ARABIC -> ContextCompat.getColor(
                holder.itemView.context,
                R.color.background_arabic
            )

            LetterType.FRENCH -> ContextCompat.getColor(
                holder.itemView.context,
                R.color.background_french
            )
        }
        holder.cardView.setCardBackgroundColor(backgroundColor)

        // Set click listener
        holder.itemView.setOnClickListener {
            onLetterClick(letter)
        }
    }

    override fun getItemCount(): Int = letters.size
}
