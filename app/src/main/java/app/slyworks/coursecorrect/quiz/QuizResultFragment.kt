package app.slyworks.coursecorrect.quiz

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import app.slyworks.coursecorrect.base.BaseActivity
import app.slyworks.coursecorrect.base.MOnBackPressedCallback
import app.slyworks.coursecorrect.base.utils.toggleVisibility
import app.slyworks.coursecorrect.databinding.FragmentQuizResultBinding
import app.slyworks.coursecorrect.home.HomeActivity
import dev.joshuasylvanus.navigator.Navigator

class QuizResultFragment : Fragment() {
    private lateinit var adapter: RVQuizResultsAdapter

    private lateinit var viewModel:QuizViewModel
    private lateinit var binding: FragmentQuizResultBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)

        viewModel = (requireActivity() as QuizActivity).viewModel
    }

    override fun onStop() {
        super.onStop()
        viewModel.resetUIState()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentQuizResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initData()
        initViews()
    }

    private fun initData(){
        viewModel.uiStateLD.observe(viewLifecycleOwner){
            when(it){
                is QuizUIState.QuizResults -> {
                  val shouldShowRV:Boolean = it.result.failedQuestions.isNotEmpty()
                  binding.rvResults.toggleVisibility(shouldShowRV)
                  binding.tvResults.setText("${it.result.correctCount}/${it.result.totalCount}")
                  if(shouldShowRV){
                      binding.tvCorrections.toggleVisibility(true)
                      adapter.setDataList(it.result.failedQuestions)
                  }
                }
                else -> {}
            }
        }

        viewModel.getQuizResults()
    }

    private fun initViews(){
        adapter = RVQuizResultsAdapter()
        binding.rvResults.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvResults.adapter = adapter

        binding.btnContinue.setOnClickListener {
            Navigator.intentFor<HomeActivity>(requireActivity())
                .newAndClearTask()
                .navigate()
        }
    }
}