package com.example.fitnesssemestralka

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fitnesssemestralka.data.ExerciseInfo

/**
 * Adapter that use RecyclerView to show all exercises when creating new plan.
 *
 * @property exerciseList
 */
class ExerciseAdapter(private var exerciseList: List<ExerciseInfo>) :
    RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>() {

    private val selectedExercisesIDs = mutableSetOf<Int>()

    inner class ExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkboxExercise: CheckBox = itemView.findViewById(R.id.checkboxExercise)
        val exerciseNameTextView: TextView = itemView.findViewById(R.id.textExerciseName)
        val exerciseImageView: ImageView = itemView.findViewById(R.id.imageExercise)
    }

    /**
     * Creating and initializing the view holder for each item in the RecyclerView.
     *
     * @param parent
     * @param viewType
     * @return
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_exercise, parent, false)
        return ExerciseViewHolder(itemView)
    }

    /**
     * Set up the view for each item.
     * Setting up the image and control the checkbox.
     * @param holder
     * @param position
     */
    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val currentExercise = exerciseList[position]

        // Bind the exercise name
        holder.exerciseNameTextView.text = currentExercise.name


        // Load the exercise image using Glide
        Glide.with(holder.itemView.context)
            .load(
                holder.itemView.context.resources.getIdentifier(
                    currentExercise.img,
                    "drawable",
                    holder.itemView.context.packageName
                )
            )
            .into(holder.exerciseImageView)

        holder.checkboxExercise.isChecked = selectedExercisesIDs.contains(currentExercise.id)
        holder.checkboxExercise.setOnClickListener {
            if (holder.checkboxExercise.isChecked) {
                selectedExercisesIDs.add(currentExercise.id)
            } else {
                selectedExercisesIDs.remove(currentExercise.id)
            }
        }
    }

    /**
     * Function that return total exercises in Recycler.
     *
     * @return
     */
    override fun getItemCount(): Int {
        return exerciseList.size
    }

    /**
     * Overwrite the List of exercises.
     *
     * @param exercises
     */
    fun updateExercises(exercises: List<ExerciseInfo>) {
        exerciseList = exercises
        notifyDataSetChanged()
    }

    /**
     * Return selected Exercises
     *
     * @return
     */
    fun getSelectedExerciseIds(): List<Int> {
        return selectedExercisesIDs.toList()
    }


}
