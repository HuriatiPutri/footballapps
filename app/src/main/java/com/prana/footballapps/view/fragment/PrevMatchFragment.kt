package com.prana.footballapps.view.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.google.gson.Gson
import com.prana.footballapps.R
import com.prana.footballapps.adapter.PrevMatchAdapter
import com.prana.footballapps.api.ApiRequest
import com.prana.footballapps.model.MatchDataItem
import com.prana.footballapps.presenter.MatchEventPresenter
import com.prana.footballapps.view.MatchEventView
import kotlinx.android.synthetic.main.fragment_prev_match.view.*
import org.jetbrains.anko.support.v4.onRefresh

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [PrevMatchFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [PrevMatchFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class PrevMatchFragment : Fragment(), MatchEventView {

    private var dataItems: MutableList<MatchDataItem> = mutableListOf()
    private var listener : OnFragmentInteractionListener? = null

    private lateinit var matchEventPresenter : MatchEventPresenter
    private lateinit var adapter             : PrevMatchAdapter
    private lateinit var swipeRefreshLayout  : SwipeRefreshLayout
    private lateinit var progressBar         : ProgressBar

    /*
    *  override fun onCreate(savedInstanceState: Bundle?) {
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
        val view = inflater.inflate(R.layout.fragment_prev_match, container, false)

        val rv = view.findViewById<RecyclerView>(R.id.rv_match_list)
        rv.layoutManager = LinearLayoutManager(context)
        adapter = PrevMatchAdapter(dataItems, listener)
        rv.adapter = adapter

        swipeRefreshLayout  = view.swipe_refresh
        progressBar         = view.progress_bar

        swipeRefreshLayout.onRefresh {
            matchEventPresenter.getMatchPrevData("4335")
        }

        showProgress()

        val apiReq  = ApiRequest()
        val gson    = Gson()
        matchEventPresenter = MatchEventPresenter(this, apiReq, gson )
//        Log.e("Data Error", "Log: " +matchEventPresenter)
        matchEventPresenter.getMatchPrevData("4335")

        return view
    }

//    // TODO: Rename method, update argument and hook method into UI event
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
        // TODO: Update argument type and name
        fun onFragmentInteraction(item: MatchDataItem)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PrevMatchFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() = PrevMatchFragment()
    }

    override fun showProgress() {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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