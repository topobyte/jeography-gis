// Copyright 2016 Sebastian Kuerten
//
// This file is part of jeography.
//
// jeography is free software: you can redistribute it and/or modify
// it under the terms of the GNU Lesser General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// jeography is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public License
// along with jeography. If not, see <http://www.gnu.org/licenses/>.

package de.topobyte.jeography.viewer.geometry.manage;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.xml.parsers.SAXParser;
import javax.xml.transform.sax.TransformerHandler;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class GeometryRulesModel implements ListModel<GeometryRule>
{

	private List<GeometryRule> rules = new ArrayList<>();
	private List<ListDataListener> listeners = new ArrayList<>();

	/**
	 * @param rule
	 *            ad this rule to the list
	 */
	public void add(GeometryRule rule)
	{
		rules.add(rule);
		int i = rules.indexOf(rule);
		ListDataEvent e = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED,
				i, i);
		for (ListDataListener listener : listeners) {
			listener.intervalAdded(e);
		}
		triggerAdd(rule);
	}

	/**
	 * Set the rules of the model to the denoted list.
	 * 
	 * @param rules
	 *            the rules to use.
	 */
	public void setRules(List<GeometryRule> rules)
	{
		this.rules = rules;
		ListDataEvent e = new ListDataEvent(this,
				ListDataEvent.CONTENTS_CHANGED, 0, 0);
		for (ListDataListener listener : listeners) {
			listener.contentsChanged(e);
		}
		triggerChange();
	}

	/**
	 * Remove the rules denoted by the given list of indices.
	 * 
	 * @param indexList
	 *            a list of list indices. Indices MUST be sorted descending.
	 */
	public void removeByIndices(ArrayList<Integer> indexList)
	{
		for (int i : indexList) {
			GeometryRule rule = rules.get(i);
			rules.remove(i);
			ListDataEvent e = new ListDataEvent(this,
					ListDataEvent.INTERVAL_REMOVED, i, i);
			for (ListDataListener listener : listeners) {
				listener.intervalRemoved(e);
			}
			triggerRemove(rule);
		}
	}

	/**
	 * Remove the denoted rule.
	 * 
	 * @param rule
	 *            the rule to remove.
	 */
	public void removeRule(GeometryRule rule)
	{
		int i = rules.indexOf(rule);
		if (i == -1) {
			return;
		}
		rules.remove(rule);
		ListDataEvent e = new ListDataEvent(this,
				ListDataEvent.INTERVAL_REMOVED, i, i);
		for (ListDataListener listener : listeners) {
			listener.intervalRemoved(e);
		}
		triggerRemove(rule);
	}

	/**
	 * Swap the to list items denoted by a and b
	 * 
	 * @param a
	 *            the first item to swap
	 * @param b
	 *            the seconds item to swap
	 */
	public void swap(int a, int b)
	{
		GeometryRule t = rules.get(a);
		rules.set(a, rules.get(b));
		rules.set(b, t);
		ListDataEvent e = new ListDataEvent(this,
				ListDataEvent.CONTENTS_CHANGED, 0, 0);
		for (ListDataListener listener : listeners) {
			listener.intervalAdded(e);
		}
		triggerReorder();
	}

	/**
	 * @return the list of rules in this model
	 */
	public List<GeometryRule> getRules()
	{
		return rules;
	}

	@Override
	public int getSize()
	{
		return rules.size();
	}

	@Override
	public GeometryRule getElementAt(int index)
	{
		return rules.get(index);
	}

	@Override
	public void addListDataListener(ListDataListener l)
	{
		listeners.add(l);
	}

	@Override
	public void removeListDataListener(ListDataListener l)
	{
		listeners.remove(l);
	}

	/**
	 * Get the rule denoted by the given name.
	 * 
	 * @param name
	 *            the name to search for.
	 * @return the found rule or null.
	 */
	public GeometryRule getRuleByName(String name)
	{
		for (GeometryRule rule : rules) {
			if (rule.getName().equals(name)) {
				return rule;
			}
		}
		return null;
	}

	List<GeometryRuleModelListener> ruleListeners = new ArrayList<>();

	/**
	 * Add a listener to be informed about changes to the model.
	 * 
	 * @param l
	 *            the listener to add.
	 */
	public void addChangeListener(GeometryRuleModelListener l)
	{
		ruleListeners.add(l);
	}

	/**
	 * Remove a listener from the list of listeners.
	 * 
	 * @param l
	 *            the listener to remove.
	 */
	public void removeChangeListener(GeometryRuleModelListener l)
	{
		ruleListeners.add(l);
	}

	private void triggerAdd(GeometryRule rule)
	{
		for (GeometryRuleModelListener l : ruleListeners) {
			l.ruleAdded(rule);
		}
	}

	private void triggerRemove(GeometryRule rule)
	{
		for (GeometryRuleModelListener l : ruleListeners) {
			l.ruleRemoved(rule);
		}
	}

	private void triggerChange(GeometryRule rule)
	{
		for (GeometryRuleModelListener l : ruleListeners) {
			l.ruleChanged(rule);
		}
	}

	private void triggerReorder()
	{
		for (GeometryRuleModelListener l : ruleListeners) {
			l.rulesReordered();
		}
	}

	private void triggerChange()
	{
		for (GeometryRuleModelListener l : ruleListeners) {
			l.rulesChanged();
		}
	}

	/**
	 * Call this to trigger an event that proclaims a complex change possibly
	 * affecting all rules.
	 */
	public void refresh()
	{
		triggerChange();
	}

	/**
	 * Call this to trigger an event that proclaims a change in the denoted
	 * rule.
	 * 
	 * @param rule
	 *            the rule that changed.
	 */
	public void refresh(GeometryRule rule)
	{
		triggerChange(rule);
	}

	/**
	 * Serialize the model to a XML output.
	 * 
	 * @param handler
	 *            the handler to output to.
	 * @throws SAXException
	 *             if a xml error occurs.
	 */
	public void serialize(TransformerHandler handler) throws SAXException
	{
		AttributesImpl atts = new AttributesImpl();
		handler.startElement("", "", "rules", atts);

		serializeRules(handler, atts);

		handler.endElement("", "", "rules");
	}

	private void serializeRules(TransformerHandler handler, AttributesImpl atts)
			throws SAXException
	{
		for (GeometryRule rule : rules) {
			atts.clear();
			atts.addAttribute("", "", "name", "CDATA", rule.getName());
			atts.addAttribute("", "", "namespace", "CDATA",
					rule.getNamespace());
			atts.addAttribute("", "", "style", "CDATA", rule.getStyle());
			handler.startElement("", "", "rule", atts);
			handler.endElement("", "", "rule");
		}
	}

	/**
	 * Read a GeometryRulesModel from the given Inputstream and SAXParser
	 * 
	 * @param sax
	 *            the parser to use.
	 * @param inputStream
	 *            the input stream to read from.
	 * @return the new list of GeometryRules.
	 * @throws SAXException
	 *             if an error occurred while reading.
	 * @throws IOException
	 *             if an error occurred while reading.
	 */
	public static List<GeometryRule> deserialize(SAXParser sax,
			FileInputStream inputStream) throws SAXException, IOException
	{
		final List<GeometryRule> rules = new ArrayList<>();

		DefaultHandler handler = new DefaultHandler() {

			GeometryRule currentRule = null;

			@Override
			public void startElement(String uri, String localName, String qName,
					Attributes atts)
			{
				if (qName.equals("rule")) {
					String name = atts.getValue("name");
					String namespace = atts.getValue("namespace");
					String style = atts.getValue("style");
					currentRule = new GeometryRule(name, namespace, style,
							new ArrayList<GeometryRuleTag>());
				}
			}

			@Override
			public void endElement(String uri, String localName, String qName)
			{
				if (qName.equals("rule")) {
					rules.add(currentRule);
				}
			}
		};
		sax.parse(inputStream, handler);

		return rules;
	}

}
