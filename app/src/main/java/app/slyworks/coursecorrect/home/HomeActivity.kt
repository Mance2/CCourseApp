package app.slyworks.coursecorrect.home

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import app.slyworks.coursecorrect.base.BaseActivity
import app.slyworks.coursecorrect.base.MOnBackPressedCallback
import app.slyworks.coursecorrect.databinding.ActivityHomeBinding
import dev.joshuasylvanus.navigator.Navigator
import dev.joshuasylvanus.navigator.interfaces.FragmentContinuationStateful


/**
 *Created by Joshua Sylvanus, 5:22 PM, 12-Apr-23.
 */
class HomeActivity : BaseActivity() {
    lateinit var navigator: FragmentContinuationStateful
    lateinit var viewModel: HomeViewModel
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initData()
        initViews()
    }

    private fun initData(){
      viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

      navigator = Navigator.transactionWithStateFrom(supportFragmentManager)

      this.onBackPressedDispatcher
            .addCallback(this, MOnBackPressedCallback(this, navigator))

    }

    private fun initViews(){
        navigator.into(binding.fragmentContainer.id)
            .show(HomeFragment())
            .navigate()
    }

    fun toggleDrawerState():Unit =
        if(binding.drawerHome.isOpen) binding.drawerHome.close() else binding.drawerHome.open()


}