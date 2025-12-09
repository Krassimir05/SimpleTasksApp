package com.example.simpletasks.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simpletasks.data.AppDatabase
import com.example.simpletasks.data.TaskRepository
import com.example.simpletasks.databinding.FragmentTaskListBinding

class TaskListFragment : Fragment() {

    private var _binding: FragmentTaskListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TaskViewModel by viewModels {
        TaskViewModelFactory(
            TaskRepository(
                AppDatabase.getInstance(requireContext()).taskDao()
            )
        )
    }

    private lateinit var adapter: TaskListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TaskListAdapter(
            onItemClick = { task ->
                val action =
                    TaskListFragmentDirections.actionTaskListFragmentToTaskDetailFragment(task.id)
                findNavController().navigate(action)
            },
            onItemLongClick = { task ->
                viewModel.deleteTask(task)
            }
        )

        binding.recyclerViewTasks.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewTasks.adapter = adapter

        binding.fabAddTask.setOnClickListener {
            val action =
                TaskListFragmentDirections.actionTaskListFragmentToTaskDetailFragment(0L)
            findNavController().navigate(action)
        }

        viewModel.tasks.observe(viewLifecycleOwner) { tasks ->
            adapter.submitList(tasks)
            binding.emptyView.visibility =
                if (tasks.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
