package com.prana.footballapps.view.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.Spinner
import com.google.gson.Gson
import com.prana.footballapps.R
import com.prana.footballapps.adapter.NextMatchAdapter
import com.prana.footballapps.api.ApiRequest
import com.prana.footballapps.model.MatchDataItem
import com.prana.footballapps.presenter.MatchEventPresenter
import com.prana.footballapps.view.MatchEventView
import kotlinx.android.synthetic.main.fragment_next_match.view.*
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.onRefresh

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [NextMatchFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [NextMatchFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class NextMatchFragment : Fragment(), MatchEventView {

    private var dataItems: MutableList<MatchDataItem> = mutableListOf()
    private var listener : OnFragmentInteractionListener? = null

    private lateinit var matchEventPresenter : MatchEventPresenter
    private lateinit var adapter             : NextMatchAdapter
    private lateinit var swipeRefreshLayout  : SwipeRefreshLayout
    private lateinit var progressBar         : ProgressBar
    private lateinit var spinner             : Spinner
    private lateinit var league              : String

/*    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
*/

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
       val view = inflater.inflate(R.layout.fragment_next_match, container, false)

        // Setting SpinnerAdapter
        spinner = view.spinner_next_match
        val spinnerItems = resources.getStringArray(R.array.league)
        val spinnerAdapter = ArrayAdapter(ctx, android.R.layout.simple_spinner_dropdown_item, spinnerItems)
        spinner.adapter = spinnerAdapter

        // Spinner onClick
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //  TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when(position) {
                    0 -> {
                        league = spinner.selectedItem.toString().replace("Spanish La Liga", "4335")
                        matchEventPresenter.getMatchNextData(league)
                    }
                    1 -> {
                        league = spinner.selectedItem.toString().replace("English Premier League", "4328")
                        matchEventPresenter.getMatchNextData(league)
                    }
                    2 -> {
                        league = spinner.selectedItem.toString().replace("English League Championship", "4329")
                        matchEventPresenter.getMatchNextData(league)
                    }
                    3 -> {
                        league = spinner.selectedItem.toString().replace("German Bundesliga", "4331")
                        matchEventPresenter.getMatchNextData(league)
                    }
                    4 -> {
                        league = spinner.selectedItem.toString().replace("Italian Serie A", "4332")
                        matchEventPresenter.getMatchNextData(league)
                    }
                    5 -> {
                        league = spinner.selectedItem.toString().replace("French Ligue 1", "4334")
                        matchEventPresenter.getMatchNextData(league)
                    }
                }
            }
        }

        val rv = view.findViewById<RecyclerView>(R.id.rv_match_next_list)
        rv.layoutManager = LinearLayoutManager(context)
        adapter = NextMatchAdapter(dataItems, listener)
        rv.adapter = adapter

        swipeRefreshLayout  = view.swipe_refresh
        progressBar         = view.progress_bar

        swipeRefreshLayout.onRefresh {
            matchEventPresenter.getMatchNextData(league)
        }
        showProgress()

        val api     = ApiRequest()
        val gson    = Gson()
        matchEventPresenter = MatchEventPresenter(this, api, gson )

        // matchEventPresenter.getMatchNextData("4335") // sudah diset dispinner

        return view
    }

    // TODO: Rename method, update argument and hook method into UI event
//    fun onButtonPressed(uri: Uri) {
//        listener?.onFragmentInteraction(uri)
//    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {

        fun onFragmentInteraction(item: MatchDataItem) // data ke Adapter
    }

    companion object {
        @JvmStatic
        fun newInstance() = NextMatchFragment()
    }

    override fun showProgress() {

        progressBar.visibility = View.VISIBLE

    }

    override fun hideProgress() {

        progressBar.visibility = View.GONE

    }

    override fun showDataMatchList(data: List<MatchDataItem>) {

        swipeRefreshLayout.isRefreshing = false
        dataItems.clear()
        dataItems.addAll(data)
        adapter.notifyDataSetChanged()
        hideProgress()

    }

}
