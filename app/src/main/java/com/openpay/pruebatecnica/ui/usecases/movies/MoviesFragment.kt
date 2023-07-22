package com.openpay.pruebatecnica.ui.usecases.movies

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.openpay.pruebatecnica.data.model.RatedMovieModel
import com.openpay.pruebatecnica.data.model.ResponseRatedMovieModel
import com.openpay.pruebatecnica.databinding.FragmentMoviesBinding
import com.openpay.pruebatecnica.ui.usecases.profile.ProfileFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MoviesFragment : Fragment() {
    companion object {
        val MY_TAG: String = MoviesFragment::class.java.simpleName
    }

    private var _binding: FragmentMoviesBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapterPopularMovies: MoviesAdapter
    private lateinit var adapterTopRatedMovies: MoviesAdapter
    private val moviesFragmentViewModel: MoviesFragmentViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)
        setupPopularMovies()
        setupTopRatedMovies()
        setupTrendingMovies()
        return binding.root
    }

    private fun setupTrendingMovies() {
        binding.recyclerViewRecommendedMovies.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        moviesFragmentViewModel.loadTrendingMovieViewModelResponse.observe(viewLifecycleOwner) {
            validateResponseLoadTrendingMovie(it)
        }
        moviesFragmentViewModel.loadTrendingMovies()
    }

    private fun validateResponseLoadTrendingMovie(responseModel: ResponseRatedMovieModel?) {
        responseModel?.let { vo ->
            val movieList = vo.results
            movieList?.let { dataLst ->
                if (dataLst.isNotEmpty()) {
                    binding.recyclerViewRecommendedMovies.adapter =
                        TrendingMoviesAdapter(dataLst, { vo -> onItemSelected(vo) }) { vo, byteArray -> savTrendingMovieImage(vo, byteArray) }
                }
            }
        }
    }

    private fun savTrendingMovieImage(vo: RatedMovieModel, byteArray: ByteArray) {
        moviesFragmentViewModel.saveTrendingMovieImage(vo, byteArray)
    }

    private fun setupPopularMovies() {
        adapterPopularMovies = MoviesAdapter({ vo -> onItemSelected(vo) }) { vo, byteArray -> saveMovieImage(vo, byteArray) }
        binding.recyclerViewPopularMovies.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewPopularMovies.adapter = adapterPopularMovies
        loadPopularMovies()
    }

    private fun loadPopularMovies() {
        viewLifecycleOwner.lifecycleScope.launch {
            moviesFragmentViewModel.loadPopularMovies().collectLatest { pagingData ->
                adapterPopularMovies.submitData(pagingData)
            }
        }
    }

    private fun saveMovieImage(vo: RatedMovieModel, byteArray: ByteArray) {
        moviesFragmentViewModel.saveMovieImage(vo, byteArray)
    }

    private fun setupTopRatedMovies() {
        adapterTopRatedMovies = MoviesAdapter({ vo -> onItemSelected(vo) }) { vo, byteArray -> saveTopRatedMovieImage(vo, byteArray) }
        binding.recyclerViewTopRatedMovies.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewTopRatedMovies.adapter = adapterTopRatedMovies
        loadTopRatedMovies()
    }

    private fun loadTopRatedMovies() {
        viewLifecycleOwner.lifecycleScope.launch {
            moviesFragmentViewModel.loadTopRatedMovies().collectLatest { pagingData ->
                adapterTopRatedMovies.submitData(pagingData)
            }
        }
    }

    private fun saveTopRatedMovieImage(vo: RatedMovieModel, byteArray: ByteArray) {
        moviesFragmentViewModel.saveTopRatedMovieImage(vo, byteArray)
    }

    private fun onItemSelected(vo: RatedMovieModel) {
        val name = vo.name
        Log.i(ProfileFragment.MY_TAG, "onItemSelected: $name}")
    }
}