package com.example.simpletasks.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.simpletasks.data.AppDatabase
import com.example.simpletasks.data.TaskRepository
import com.example.simpletasks.databinding.FragmentTaskDetailBinding

class TaskDetailFragment : Fragment() {

    private var _binding: FragmentTaskDetailBinding? = null
    private val binding get() = _binding!!

    private val args: TaskDetailFragmentArgs by navArgs()

    private val viewModel: TaskViewModel by viewModels {
        TaskViewModelFactory(
            TaskRepository(
                AppDatabase.getInstance(requireContext()).taskDao()
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = args.taskId
        if (id != 0L) {
            viewModel.loadTask(id)
        } else {
            viewModel.clearSelectedTask()
        }

        viewModel.selectedTask.observe(viewLifecycleOwner) { task ->
            if (task != null) {
                binding.editTextTitle.setText(task.title)
                binding.editTextDescription.setText(task.description)
                binding.checkBoxDone.isChecked = task.isDone
            } else {
                binding.editTextTitle.setText("")
                binding.editTextDescription.setText("")
                binding.checkBoxDone.isChecked = false
            }
        }

        binding.buttonSave.setOnClickListener {
            val title = binding.editTextTitle.text.toString()
            val description = binding.editTextDescription.text.toString()
            val isDone = binding.checkBoxDone.isChecked

            viewModel.saveTask(
                id = if (id == 0L) null else id,
                title = title,
                description = description,
                isDone = isDone,
                onError = { msg ->
                    Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
                },
                onSuccess = {
                    Toast.makeText(requireContext(), "Записано!", Toast.LENGTH_SHORT).show()
                    findNavController().navigateUp()
                }
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
