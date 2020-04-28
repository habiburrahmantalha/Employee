package com.tigerit.employee.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.tigerit.employee.BR
import com.tigerit.employee.R
import com.tigerit.employee.databinding.LayoutEmployeeItemBinding
import com.tigerit.employee.ui.ViewHolderEmpty
import com.tigerit.employee.ui.ViewHolderShimmer
import com.tigerit.employee.ui.ViewTypes
import com.tigerit.employee.ui.ViewTypes.*


class EmployeeAdapter(var list: List<Employee?>, val viewModel: HomeVM): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            REGULAR.value ->  ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.layout_employee_item, parent, false))
            else ->ViewHolderEmpty(LayoutInflater.from(parent.context).inflate(
                R.layout.layout_empty,
                parent,
                false)
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            list.isNullOrEmpty() -> EMPTY.value
            else -> REGULAR.value
        }
    }

    override fun getItemCount(): Int {
        return  when {
            list.isNullOrEmpty() -> 1
            else -> list.size
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is ViewHolder -> {
                list[position]?.let {
                    holder.bind(it, viewModel)
                }
            }
        }
    }

    inner class ViewHolder(private val binding: LayoutEmployeeItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(data:Any, viewModel: Any){
            binding.setVariable(BR.employee, data)
            binding.setVariable(BR.viewModel, viewModel)
            binding.executePendingBindings()
        }
    }

    fun updateList(it: List<Employee?>?) {
        it?.let {
            list = it
        }
        notifyDataSetChanged()
    }
}