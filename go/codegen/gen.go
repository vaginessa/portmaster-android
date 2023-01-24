package main

import (
	"fmt"
	"go/ast"
	"go/parser"
	"go/token"
	"os"
	"strings"
)

const subPackage = "../engine"

const fileTemplate = `package io.safing.portmaster.android.ui;

// DO NOT EDIT THIS FILE!
// The file was autogenerated by go/codegen/gen.go

import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

@CapacitorPlugin(name = "GoBridge")
public class GoBridge extends Plugin {
%s
}
`

const MethodTemplate = `
    @PluginMethod()
    public void %s(PluginCall call) {
        engine.Engine.%s(new GoPluginCall(call));
    }
`

func getJavaMethod(f *ast.FuncDecl) string {
	camelCaseFunc := strings.ToLower(f.Name.String()[0:1]) + f.Name.String()[1:]
	method := fmt.Sprintf(MethodTemplate, f.Name, camelCaseFunc)
	return method
}

func hasPluginCallParam(f *ast.FuncDecl) bool {
	if f == nil || f.Type == nil {
		return false
	}

	// Check if the function has exactly one parameter
	if f.Type.Params == nil || len(f.Type.Params.List) != 1 {
		return false
	}

	// Check if the type of the first (and only) parameter is PluginCall
	return fmt.Sprintf("%s", f.Type.Params.List[0].Type) == "PluginCall"
}

func main() {
	argsProg := os.Args[1:]

	if len(argsProg) != 1 {
		fmt.Println("Invalid arguments")
		return
	}

	set := token.NewFileSet()
	packs, err := parser.ParseDir(set, subPackage, nil, 0)
	if err != nil {
		fmt.Println("Failed to parse package:", err)
		os.Exit(1)
	}

	funcs := []*ast.FuncDecl{}
	for _, pack := range packs {
		for _, f := range pack.Files {
			for _, d := range f.Decls {
				if fn, isFn := d.(*ast.FuncDecl); isFn {
					funcs = append(funcs, fn)
				}
			}
		}
	}

	functions := ""
	for _, f := range funcs {
		if hasPluginCallParam(f) {
			functions += getJavaMethod(f)
		}
	}

	// f, err := os.Create()
	f, err := os.Create(argsProg[0])
	if err != nil {
		fmt.Printf("Failed to create file: %s", err)
		return
	}
	defer f.Close()
	_, err = f.WriteString(fmt.Sprintf(fileTemplate, functions))
	if err != nil {
		fmt.Printf("Failed to write to file: %s", err)
		return
	}
}
