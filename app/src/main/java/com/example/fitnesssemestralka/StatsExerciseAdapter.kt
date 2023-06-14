package com.example.fitnesssemestralka

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fitnesssemestralka.data.ExerciseInfo

/**
 * Class is a RecyclerView adapter used to populate and manage a list of exercise items in the StatsExerciseFragment.
 *
 * @property exerciseList
 * @property navController
 */
class StatsExerciseAdapter(private var exerciseList: List<ExerciseInfo>,private val navController: NavController) :
    RecyclerView.Adapter<StatsExerciseAdapter.StatsExerciseViewHolder>() {

    inner class StatsExerciseViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView) {
        val exerciseNameTextView: TextView = itemView.findViewById(R.id.textExerciseStat)
        val exerciseImageView: ImageView = itemView.findViewById(R.id.imageExerciseStat)
        }

    /**
     * Inflating the item_exercise_stat layout and creating a StatsExerciseViewHolder instance with the inflated view.
     *
     * @param parent
     * @param viewType
     * @return
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatsExerciseViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_exercise_stat, parent, false)
        return StatsExerciseViewHolder(itemView)
    }

    /**
     * Update the exerciseList with a new list of ExerciseInfo objects.
     *
     * @param exercises
     */
    fun updateExercises(exercises: List<ExerciseInfo>) {
        exerciseList = exercises
        notifyDataSetChanged()
    }

    /**
     * Binding the data at the specified position in the exerciseList to the corresponding views in the StatsExerciseViewHolder.
     * Sets the exercise name and loads the exercise image using Glide.
     *
     * @param holder
     * @param position
     */
    override fun onBindViewHolder(holder: StatsExerciseViewHolder, position: Int) {
        val exercise = exerciseList[position]
        holder.exerciseNameTextView.text = exercise.name
        Glide.with(holder.itemView.context)
            .load(
                holder.itemView.context.resources.getIdentifier(
                    exercise.img,
                    "drawable",
                    holder.itemView.context.packageName
                )
            )
            .into(holder.exerciseImageView)
        holder.itemView.setOnClickListener{
            val bundle = Bundle()
            bundle.putString("Name",holder.exerciseNameTextView.text.toString())
            navController.navigate(R.id.detailsExercise,bundle)//stats profile to tam kde bude graf
        }
    }

    /**
     *  Returns the size of the exerciseList.
     *
     * @return
     */
    override fun getItemCount(): Int {
        return exerciseList.size
    }
}
