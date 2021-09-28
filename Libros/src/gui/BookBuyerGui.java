/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import agents.BookBuyerAgent;
import behaviours.RequestPerformer;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;
/**
 *
 * @author Valeria
 */
public class BookBuyerGui extends JFrame{
    private String bookTitle, bp; 
    private BookBuyerAgent myAgent;
    private RequestPerformer rq;
    private JTextField titulin;
    private JTextArea precioF;
	
	public BookBuyerGui(BookBuyerAgent a, String price) {
	    myAgent = a;
            bp = price;
	}
	public void gui(String price){
            bookTitle = String.valueOf(myAgent.getBookTitle());
            /*JPanel p = new JPanel();
            p.setLayout (new GridLayout(2,2));
            p.add(new JLabel("Libro: "));
            titulin = new JTextField(10);
            p.add(titulin);
            p.add(new JLabel("Precio: "));
            precioF = new JTextArea(6,30);
            p.add(precioF);
            titulin.setText(bookTitle);
            precioF.setText(getPrice(price));
            p.setVisible(true);
            */
            int dialog=JOptionPane.showConfirmDialog(null,"Libro : "+ bookTitle +"\n\n\n"+ " Mejor Precio: " + getPrice(price));
            if (JOptionPane.OK_OPTION == dialog){
                System.out.println("Si");
            }
            else{
                System.out.println("No");
            }
        }
        public String getPrice(String price){
            String prec = price;
            return prec;
        }
	public void main(String args[]){
            gui(bp);  
        }

}
