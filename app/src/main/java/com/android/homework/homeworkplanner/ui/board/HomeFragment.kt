package com.android.homework.homeworkplanner.ui.board

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.homework.homeworkplanner.AddHomeworkActivity
import com.android.homework.homeworkplanner.data.Homework
import com.android.homework.homeworkplanner.databinding.FragmentHomeBinding
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase


class HomeFragment : Fragment() {

    private lateinit var database: DatabaseReference
    lateinit var progress_circular: CircularProgressIndicator
    lateinit var recyclerView: RecyclerView
    lateinit var homeworks: ArrayList<Homework>
    lateinit var homeworkAdapter: HomeworkAdapter
    lateinit var searchBar: SearchView

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        recyclerView = binding.recyclerView
        progress_circular = binding.progressCircular
        searchBar = binding.searchBar

        recyclerView.layoutManager = LinearLayoutManager(activity)
        homeworks = ArrayList()

        binding.addHomework.setOnClickListener { _ ->
            activity?.let{
                val intent = Intent (it, AddHomeworkActivity::class.java)
                it.startActivity(intent)
            }
        }

        database = Firebase.database.reference
        val homeworksRef: DatabaseReference = database.child("homeworks")

        homeworksRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                homeworkAdapter =
                    HomeworkAdapter(homeworks)
                homeworkAdapter.notifyDataSetChanged()
                homeworks.clear()
                for (postSnapshot in dataSnapshot.children) {
                    val homework: Homework? = postSnapshot.getValue<Homework>()
                    Log.d("LOG","hello " + (homework?.date ?: ""))
                    homework?.let { homeworks.add(it) }
                }

                searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        homeworkAdapter.getFilter().filter(query)
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        homeworkAdapter.getFilter().filter(newText);
                        return true
                    }

                })

                recyclerView.adapter = homeworkAdapter
                homeworkAdapter.notifyDataSetChanged()
                progress_circular.visibility = View.GONE
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}