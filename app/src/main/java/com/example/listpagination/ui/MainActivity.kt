package com.example.listpagination.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.listpagination.R
import com.example.listpagination.api.PostDataSource
import com.example.listpagination.api.PostService
import com.example.listpagination.data.repository.PostRepository
import com.example.listpagination.databinding.ActivityMainBinding
import com.example.listpagination.ui.adapter.CustomLoadStateAdapter
import com.example.listpagination.ui.adapter.PostAdapter
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: PostAdapter
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: PostsViewModel
    private var selectPosts: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolBar)
        supportActionBar?.title = getString(R.string.app_name)
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(PostRepository(PostDataSource(PostService.create(this))))
        )[PostsViewModel::class.java]
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        adapter = PostAdapter(this@MainActivity)
        binding.postRecycler.adapter = adapter

        // add dividers between RecyclerView's row items
        val decoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        binding.postRecycler.addItemDecoration(decoration)

        selectPosts?.cancel()
        selectPosts = lifecycleScope.launch {
            viewModel.posts.collectLatest {
                adapter.submitData(it)
            }
        }

        binding.postRecycler.adapter = adapter.withLoadStateFooter(
            footer = CustomLoadStateAdapter {
                adapter.retry()
            }
        )
        lifecycleScope.launch {
            adapter.loadStateFlow.collect { loadState ->
                val refreshState = loadState.refresh

                // Only show the list if refresh succeeds.
                binding.postRecycler.isVisible = refreshState is LoadState.NotLoading
                binding.progressBar.isVisible = refreshState is LoadState.Loading
                binding.layoutError.isVisible = refreshState is LoadState.Error

                if (refreshState is LoadState.Error)
                    when (refreshState.error as Exception) {
                        is HttpException ->
                            binding.labelError.text = getString(R.string.internal_error)
                        is IOException ->
                            binding.labelError.text = getString(R.string.label_no_internet)
                    }

                val errorState = loadState.append as? LoadState.Error
                    ?: loadState.prepend as? LoadState.Error
                errorState?.let {
                    Toast.makeText(
                        this@MainActivity,
                        getString(R.string.error_text_label),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        binding.reloadPostsBtn.setOnClickListener {
            adapter.refresh()
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.swipeRefreshLayout.isRefreshing = false
            adapter.refresh()
        }
    }
}