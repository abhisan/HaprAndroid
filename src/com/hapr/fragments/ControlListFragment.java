package com.hapr.fragments;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.hapr.customview.BinderData;
import com.hapr.customview.SampleActivity;
import com.technotalkative.viewstubdemo.R;

public class ControlListFragment extends ListFragment {

	static final String KEY_TAG = "optiondata"; // parent node
	static final String KEY_ID = "id";
	static final String KEY_CONTROL = "control";
	static final String KEY_LOCATION = "location";
	static final String KEY_STATE = "state";
	static final String KEY_ICON = "icon";

	// List items
	ListView list;
	BinderData adapter = null;
	List<HashMap<String, String>> optionDataCollection;

	int fragNum;
	String arr[] = { "This is", "a Truiton", "Demo", "App", "For", "Showing", "FragmentPagerAdapter", "and ViewPager", "Implementation" };

	public static ControlListFragment init(int val) {
		ControlListFragment truitonList = new ControlListFragment();
		// Supply val input as an argument.
		Bundle args = new Bundle();
		args.putInt("val", val);
		truitonList.setArguments(args);
		return truitonList;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fragNum = getArguments() != null ? getArguments().getInt("val") : 1;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View layoutView = inflater.inflate(R.layout.fragment_pager_list, container, false);
		View tv = layoutView.findViewById(R.id.text);
		((TextView) tv).setText("Truiton Fragment #" + fragNum);

		return layoutView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// setListAdapter(new ArrayAdapter<String>(getActivity(),
		// android.R.layout.simple_list_item_1, arr));
		try {
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(getActivity().getAssets().open("controldata.xml"));
			optionDataCollection = new ArrayList<HashMap<String, String>>();
			// normalize text representation
			doc.getDocumentElement().normalize();
			NodeList optionList = doc.getElementsByTagName("optiondata");
			HashMap<String, String> map = null;
			for (int i = 0; i < optionList.getLength(); i++) {
				map = new HashMap<String, String>();
				Node firstOptionNode = optionList.item(i);
				if (firstOptionNode.getNodeType() == Node.ELEMENT_NODE) {
					Element firstWeatherElement = (Element) firstOptionNode;
					NodeList idList = firstWeatherElement.getElementsByTagName(KEY_ID);
					Element firstIdElement = (Element) idList.item(0);
					NodeList textIdList = firstIdElement.getChildNodes();
					map.put(KEY_ID, ((Node) textIdList.item(0)).getNodeValue().trim());
					NodeList cityList = firstWeatherElement.getElementsByTagName(KEY_CONTROL);
					Element firstCityElement = (Element) cityList.item(0);
					NodeList textCityList = firstCityElement.getChildNodes();
					map.put(KEY_CONTROL, ((Node) textCityList.item(0)).getNodeValue().trim());
					NodeList tempList = firstWeatherElement.getElementsByTagName(KEY_LOCATION);
					Element firstTempElement = (Element) tempList.item(0);
					NodeList textTempList = firstTempElement.getChildNodes();
					map.put(KEY_LOCATION, ((Node) textTempList.item(0)).getNodeValue().trim());
					NodeList iconList = firstWeatherElement.getElementsByTagName(KEY_ICON);
					Element firstIconElement = (Element) iconList.item(0);
					NodeList textIconList = firstIconElement.getChildNodes();
					map.put(KEY_ICON, ((Node) textIconList.item(0)).getNodeValue().trim());
					optionDataCollection.add(map);
				}
			}
			BinderData bindingData = new BinderData(getActivity(), optionDataCollection);
			// list = (ListView)
			// layoutView.findViewById(android.R.layout.simple_list_item_1);
			// setListAdapter(new ArrayAdapter<String>(getActivity(),
			// android.R.layout.simple_list_item_1, arr));
			setListAdapter(bindingData);

			// list.setAdapter(bindingData);
			this.list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Intent i = new Intent();
					i.setClass(getActivity(), SampleActivity.class);
					i.putExtra("position", String.valueOf(position + 1));
					i.putExtra("control", optionDataCollection.get(position).get(KEY_CONTROL));
					i.putExtra("location", optionDataCollection.get(position).get(KEY_LOCATION));
					i.putExtra("state", optionDataCollection.get(position).get(KEY_STATE));
					i.putExtra("icon", optionDataCollection.get(position).get(KEY_ICON));
					startActivity(i);
				}
			});
		} catch (IOException ex) {
			Log.e("Error", ex.getMessage());
		} catch (Exception ex) {
			Log.e("Error", "Loading exception");
		}

	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Log.i("Truiton FragmentList", "Item clicked: " + id);
	}
}