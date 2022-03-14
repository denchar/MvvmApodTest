package com.example.mvvmApodTest

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.mvvmApodTest.databinding.FragmentExampleBinding
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


public class ExampleFragment : Fragment(R.layout.fragment_example), SearchPictureAdapter.AddInDbClick,
    SearchPictureAdapter.ItemClick, SearchPictureAdapter.DelInDbClick {

    private var _binding: FragmentExampleBinding? = null
    private val binding get() = _binding!!
    private val searchPictureViewModel: SearchPictureViewModel2 by viewModel()
    private val teg = "SearchPictureFragment"
    private val adapter: SearchPictureAdapter by inject()
    private var errorText: TextView? = null

    private var spinnerYear: Spinner? = null
    private var spinnerMonth: Spinner? = null
    private var spinnerDay: Spinner? = null
    private var progressBarSearchPicture: ProgressBar? = null
    private var frameLayout: FrameLayout? = null
    private var swipeContainer: SwipeRefreshLayout? = null
    private var searchButton: Button? = null
    private var recyclerView: RecyclerView? = null


    private val yearList = ArrayList<String>()
    private val dayList = ArrayList<String>()
    private val monthList = listOf(
        "01", "02", "03", "04", "05", "06",
        "07", "08", "09", "10", "11", "12"
    )
    var choseYear = "1995"
    var choseMonth = "06"
    var choseDay = "20"


    override fun onItemClick(item: PictureOfDayModel?) {
        if (item != null) {
            Values.pictureOfDayModel = item
            searchPictureViewModel.onInteraction(Interaction.ItemClick(true))
            searchPictureViewModel.onInteraction(Interaction.ItemClick(false))
           // Toast.makeText(context,"${item.title}",Toast.LENGTH_SHORT).show()
        }
    }

    override fun onAddInDbClick(item: PictureOfDayModel?) {
        if (item != null) {
            Toast.makeText(context, "${item.title} add in db", Toast.LENGTH_SHORT).show()
            Values.pictureOfDayWhoNeedAddInDB = item
            searchPictureViewModel.onInteraction(Interaction.DataDataBaseChange(item.date))
            searchPictureViewModel.onInteraction(Interaction.AddPictureOfDayInDataBaseClick)
        }
    }

    override fun onDelInDbClick(item: PictureOfDayModel?) {
        if (item != null) {
            Toast.makeText(context, "${item.title} delete in db", Toast.LENGTH_SHORT).show()
            searchPictureViewModel.onInteraction(Interaction.DataDataBaseChange(item.date))
            searchPictureViewModel.onInteraction(Interaction.DeleteItemInDBClick)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExampleBinding.inflate(inflater, container, false)
        val view = binding.root

        adapter.setItemClickListener(this)
        adapter.setAddButtonClickListener(this)
        adapter.setDeleteButtonClickListener(this)

        searchButton = binding.searchImageButton
        recyclerView = binding.searchRecyclerView
        recyclerView?.adapter = adapter
        errorText = binding.badRequestTextView
        spinnerYear = binding.yearSpinner
        spinnerMonth = binding.monthSpinner
        spinnerDay = binding.daySpinner
        progressBarSearchPicture = binding.progressBarSearchPicture
        frameLayout = binding.frameLayout
        swipeContainer = binding.swipeNotificationContainer


        binding.swipeNotificationContainer.setOnRefreshListener {
            Toast.makeText(parentFragment?.context, "refreshing", Toast.LENGTH_SHORT).show()
            Thread.sleep(500)
            binding.swipeNotificationContainer.isRefreshing = false
        }

        binding.swipeNotificationContainer.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )
        setInteractions()
        observeViewState()
        addYearList()
        addDayList()
        addDateSpinners()

        return view
    }

    private fun observeViewState() {
        searchPictureViewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            // render(viewState ?: return@Observer)
             render(viewState)
            Log.d(this.javaClass.name, """FragmentViewState = ${viewState.pictureOfDay},
                |${viewState.choseYear}, ${viewState.choseMonth}, ${viewState.choseDay},
                |${viewState.choseDataDataBase}, ${viewState.errorMessage}, ${viewState.listWithDate}
            """.trimMargin())
        })
    }

    private fun render(viewState: ViewState) {
        Log.d(teg, viewState.pictureOfDay.title)

        binding.swipeNotificationContainer.isRefreshing = false

        if(viewState.itemClick){
            Toast.makeText(context,"${viewState.pictureOfDay.title}",Toast.LENGTH_SHORT).show()
           // Navigation.findNavController(binding.root).navigate(R.id.navigateToSecondFragment)
            findNavController().navigate(R.id.action_exampleFragment_to_secondFragment)
            //  activity?.startActivity(Intent(context,MainActivity2::class.java))
        }

        if(viewState.errorMessage != ""){
            Log.d(teg, "viewState.err != ")
            binding.badRequestTextView.text = viewState.errorMessage
            recyclerView?.visibility = View.INVISIBLE
            binding.badRequestTextView.visibility = View.VISIBLE
            searchButton?.isEnabled = true
        }else if(viewState.isLoading == true){
            binding.frameLayout.visibility = View.GONE
            binding.progressBarSearchPicture.visibility = View.VISIBLE
            binding.badRequestTextView.visibility = View.INVISIBLE
            searchButton?.isEnabled = false
        }else if(viewState.isLoading ==false){
            binding.frameLayout.visibility = View.VISIBLE
            binding.progressBarSearchPicture.visibility = View.INVISIBLE
            searchButton?.isEnabled = true
            recyclerView?.visibility = View.VISIBLE
            binding.badRequestTextView.visibility = View.INVISIBLE
            Values.listWithDate.value?.let { adapter.setDateList(it as ArrayList<String>) }

        }else{

        }

        adapter.setData(listOf(viewState.pictureOfDay))


    }

    private fun setInteractions() {
        searchButton?.setOnClickListener {
            val convertDate = "$choseYear-$choseMonth-$choseDay"

            searchPictureViewModel.onInteraction(Interaction.ErrorMessageChanged(""))
            searchPictureViewModel.onInteraction(Interaction.YearChange(choseYear))
            searchPictureViewModel.onInteraction(Interaction.MonthChange(choseMonth))
            searchPictureViewModel.onInteraction(Interaction.DayChange(choseDay))
            searchPictureViewModel.onInteraction(Interaction.SearchPictureClick)
        }


    }

    private fun addDateSpinners() {
        val adapterYear: ArrayAdapter<String> =
            ArrayAdapter(
                requireContext().applicationContext,
                android.R.layout.simple_spinner_item,
                yearList
            )
        adapterYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerYear?.adapter = adapterYear
        spinnerYear?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                itemSelected: View?, selectedItemPosition: Int, selectedId: Long
            ) {
                val choose = yearList
                choseYear = choose[selectedItemPosition]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        val adapterMonth: ArrayAdapter<String> =
            ArrayAdapter(
                requireContext().applicationContext,
                android.R.layout.simple_spinner_item,
                monthList
            )
        adapterMonth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMonth?.adapter = adapterMonth
        spinnerMonth?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                itemSelected: View?, selectedItemPosition: Int, selectedId: Long
            ) {
                val choose = monthList
                choseMonth = choose[selectedItemPosition]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        val adapterDay: ArrayAdapter<String> =
            ArrayAdapter(
                requireContext().applicationContext,
                android.R.layout.simple_spinner_item,
                dayList
            )
        adapterDay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerDay?.adapter = adapterDay
        spinnerDay?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                itemSelected: View?, selectedItemPosition: Int, selectedId: Long
            ) {
                val choose = dayList
                choseDay = choose[selectedItemPosition]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun addDayList() {
        for (item in 1..31) {
            dayList.add(item.toString())
        }
    }

    @SuppressLint("SimpleDateFormat", "NewApi")
    private fun addYearList() {
        var yearStep = 0
        val calendar: Calendar = Calendar.getInstance()
        val date = calendar.time
        val dateFormat = SimpleDateFormat("YYYY")
        var stringYear = dateFormat.format(date)
        while (stringYear != "1995") {
            stringYear = getYears(yearStep)
            yearStep++
            yearList.add(stringYear)
        }
    }

    @SuppressLint("NewApi", "SimpleDateFormat")
    private fun getYears(year: Int): String {

        val calendar: Calendar = Calendar.getInstance()
        calendar.add(Calendar.YEAR, -year)
        val date = calendar.time
        val dateFormat = SimpleDateFormat("YYYY")
        return dateFormat.format(date)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}