package com.example.fitnesssemestralka

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnesssemestralka.data.ExerciseInfo
import com.example.fitnesssemestralka.data.GymPlan

/**
 * Adapter class used to populate a RecyclerView with gym plans.
 *
 * @property gymPlans
 * @property navController
 */
class PlanAdapter(
    private var gymPlans: List<GymPlan>,
    private val navController: NavController
) : RecyclerView.Adapter<PlanAdapter.PlanViewHolder>() {
    /**
     * Holds references to the views within each item of the RecyclerView.
     *
     * @constructor
     * @param itemView
     */
    inner class PlanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val planNameTextView: TextView = itemView.findViewById(R.id.planNameTextView)
    }

    /**
     * Method is called when a new ViewHolder needs to be created for a RecyclerView item
     *
     * @param parent
     * @param viewType
     * @return
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_plan, parent, false)
        return PlanViewHolder(itemView)
    }

    /**
     * Function is called to bind data to the views within each item of the RecyclerView.
     * It retrieves the GymPlan object at the specified position, sets the plan name to the planNameTextView,
     * and attaches a click listener to the item.
     *
     * @param holder
     * @param position
     */
    override fun onBindViewHolder(holder: PlanViewHolder, position: Int) {
        val gymPlan = gymPlans[position]
        holder.planNameTextView.text = gymPlan.name

        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("Key",holder.planNameTextView.text.toString())
            navController.navigate(R.id.action_startPlans_to_exerciseDetailsFragment,bundle)
        }
    }

    /**
     *  Method returns the total number of gym plans in the gymPlans list.
     *
     * @return
     */
    override fun getItemCount(): Int {
        return gymPlans.size
    }

    /**
     * Method is used to update the gymPlans list with a new list of GymPlan objects.
     * It updates the list and notifies the adapter that the data has changed, triggering a rebind of the views.
     *
     * @param plans
     */
    fun updateGymPlans(plans: List<GymPlan>) {
        gymPlans = plans
        notifyDataSetChanged()
    }
}

